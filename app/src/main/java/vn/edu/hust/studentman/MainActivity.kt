package vn.edu.hust.studentman

import android.app.AlertDialog
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),
  AddStudentFragment.IAddStudentInteraction,
  ListStudentFragment.IListStudentInteraction,
  EditStudentFragment.IEditStudentInteraction {
  private lateinit var db: SQLiteDatabase


  private lateinit var _students: MutableList<StudentModel>;
  private lateinit var _studentAdapter: StudentAdapter;

  private var deletedStudent: StudentModel? = null;
  private var editingStudent: StudentModel? = null;
  private var deletedPosition: Int = -1;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    db = SQLiteDatabase.openDatabase(this.filesDir.path + "/student_man", null,
      SQLiteDatabase.CREATE_IF_NECESSARY)

    val createTable = """
      CREATE TABLE IF NOT EXISTS students(
      studentId TEXT PRIMARY KEY,
      studentName TEXT
      )
    """.trimIndent()
    db.execSQL(createTable)

    _students = mutableListOf()
    loadStudents()
  }

  override fun onDestroy()
  {
    super.onDestroy()
    if(::db.isInitialized && db.isOpen){
      db.close()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.appbar_menu, menu)
    return true;
  }

  override fun onOptionsItemSelected(item: MenuItem): Boolean {
    return when (item.itemId) {
      R.id.action_add -> {
        supportFragmentManager.beginTransaction()
          .replace(R.id.fragmentContainerView, AddStudentFragment())
          .addToBackStack("ADD_STUDENT")
          .commit()
        true
      }
      else -> super.onOptionsItemSelected(item)
    }
  }

  override fun onCreateContextMenu(
    menu: ContextMenu?,
    v: View?,
    menuInfo: ContextMenu.ContextMenuInfo?
  ) {
    menuInflater.inflate(R.menu.student_context_menu, menu);
    super.onCreateContextMenu(menu, v, menuInfo);
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    val pos = (item.menuInfo as AdapterContextMenuInfo).position;

    when (item.itemId) {
      R.id.action_edit -> {
        moveToEditFragment(_students[pos]);
      }
      R.id.action_delete -> {
        showDeleteDialog(_students[pos], pos)
      }
    }
    return super.onContextItemSelected(item)
  }

  private fun moveToEditFragment(student: StudentModel) {
    editingStudent = student
    supportFragmentManager.beginTransaction()
      .replace(R.id.fragmentContainerView, EditStudentFragment.newInstance(student.studentName, student.studentId))
      .addToBackStack("EDIT_STUDENT")
      .commit()
  }

  private fun showDeleteDialog(student: StudentModel, position: Int) {
    AlertDialog.Builder(this)
      .setTitle("Delete student?")
      .setPositiveButton("OK") { _, _ ->
        deletedStudent = student
        deletedPosition = position

        db.beginTransaction()
        try{
          val query = """DELETE FROM students WHERE studentId = '${deletedStudent!!.studentId}'"""
          db.execSQL(query)
          db.setTransactionSuccessful()

          loadStudents()
          _studentAdapter.notifyDataSetChanged()

          val rootView = findViewById<View>(android.R.id.content)
          Snackbar.make(rootView, "Deleted successfully", Snackbar.LENGTH_LONG)
            .show()
        }catch(e: Exception){
          e.printStackTrace()
          Toast.makeText(this, "DELETE ERROR", Toast.LENGTH_SHORT).show()
        }finally {
          db.endTransaction()
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  override fun onCancelAdd() {
    supportFragmentManager.popBackStack();
  }

  override fun onAddStudent(hoten: String, mssv: String) {

    if (hoten != null && mssv != null) {
      db.beginTransaction()
      try {
        val query = "INSERT OR REPLACE INTO students (studentId, studentName) VALUES (?, ?)";

        db.execSQL(query, arrayOf(mssv, hoten))
        db.setTransactionSuccessful()

        loadStudents()
        _studentAdapter.notifyDataSetChanged()

        supportFragmentManager.popBackStack();

        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "Added new student", Snackbar.LENGTH_LONG)
          .show()
      } catch (e: Exception) {
        e.printStackTrace()
        Toast.makeText(this, "Database Error!", Toast.LENGTH_SHORT).show()
      } finally {
        db.endTransaction()
      }
    }
  }

  override fun getListStudent(): MutableList<StudentModel> {
    return _students;
  }

  override fun setStudentAdapter(studentAdapter: StudentAdapter) {
    _studentAdapter = studentAdapter
  }

  override fun onCancelEdit() {
    supportFragmentManager.popBackStack();
  }

  override fun onEditStudent(hoten: String, mssv: String) {
    if (editingStudent == null) {
      Snackbar.make(findViewById(android.R.id.content), "Invalid student position", Snackbar.LENGTH_LONG).show()
      return
    } else {
      if(hoten != null && mssv != null ){
        db.beginTransaction()
        try{
          val query = """
                        update students
                        set studentName = '$hoten', studentId='$mssv'
                        where studentId = '${editingStudent!!.studentId}'
                    """.trimIndent()
          db.execSQL(query)
          db.setTransactionSuccessful()

          loadStudents()
          _studentAdapter.notifyDataSetChanged()

          supportFragmentManager.popBackStack();
          val rootView = findViewById<View>(android.R.id.content)
          Snackbar.make(rootView, "Update successfully", Snackbar.LENGTH_LONG)
            .show()
        }catch(e: Exception){
          Toast.makeText(this, "Database Error", Toast.LENGTH_SHORT).show()
          e.printStackTrace()
        }finally {
          db.endTransaction()
        }
      }
    }
  }

  private fun loadStudents(){
    _students.clear()
    val cs = db.query(
      "students",
      arrayOf("studentId", "studentName"),
      null,
      null,
      null,
      null,
      null)
    cs.moveToFirst()
    if (cs.moveToFirst()) {
      do {
        val id = cs.getString(0)
        val name = cs.getString(1)
        _students.add(StudentModel(name, id))
      } while (cs.moveToNext())
    }
    cs.close()
  }
}