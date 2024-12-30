package vn.edu.hust.studentman

import android.os.Bundle
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.room.Room
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MainActivity : AppCompatActivity(),
  AddStudentFragment.IAddStudentInteraction,
  ListStudentFragment.IListStudentInteraction,
  EditStudentFragment.IEditStudentInteraction {

  private lateinit var db: AppDatabase
  private lateinit var studentDao: StudentDao
  private lateinit var _students: MutableList<StudentModel>
  private lateinit var _studentAdapter: StudentAdapter

  private var deletedStudent: StudentModel? = null
  private var editingStudent: StudentModel? = null
  private var deletedPosition: Int = -1

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    // Initialize Room database
    db = Room.databaseBuilder(
      applicationContext,
      AppDatabase::class.java,
      "student_database"
    ).build()

    studentDao = db.studentDao()
    _students = mutableListOf()

    // Load students on app start
    CoroutineScope(Dispatchers.IO).launch {
      loadStudents()
    }
  }

  override fun onCreateOptionsMenu(menu: Menu?): Boolean {
    menuInflater.inflate(R.menu.appbar_menu, menu)
    return true
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
    menuInflater.inflate(R.menu.student_context_menu, menu)
    super.onCreateContextMenu(menu, v, menuInfo)
  }

  override fun onContextItemSelected(item: MenuItem): Boolean {
    val pos = (item.menuInfo as AdapterContextMenuInfo).position

    when (item.itemId) {
      R.id.action_edit -> {
        moveToEditFragment(_students[pos])
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
      .replace(
        R.id.fragmentContainerView,
        EditStudentFragment.newInstance(student.studentName, student.studentId)
      )
      .addToBackStack("EDIT_STUDENT")
      .commit()
  }

  private fun showDeleteDialog(student: StudentModel, position: Int) {
    android.app.AlertDialog.Builder(this)
      .setTitle("Delete student?")
      .setPositiveButton("OK") { _, _ ->
        deletedStudent = student
        deletedPosition = position

        CoroutineScope(Dispatchers.IO).launch {
          try {
            studentDao.deleteStudent(StudentModel(student.studentId, student.studentName))
            loadStudents()

            withContext(Dispatchers.Main) {
              _studentAdapter.notifyDataSetChanged()
              Snackbar.make(
                findViewById(android.R.id.content),
                "Deleted successfully",
                Snackbar.LENGTH_LONG
              ).show()
            }
          } catch (e: Exception) {
            withContext(Dispatchers.Main) {
              Toast.makeText(this@MainActivity, "DELETE ERROR", Toast.LENGTH_SHORT).show()
            }
            e.printStackTrace()
          }
        }
      }
      .setNegativeButton("Cancel", null)
      .show()
  }

  override fun onCancelAdd() {
    supportFragmentManager.popBackStack()
  }

  override fun onAddStudent(hoten: String, mssv: String) {
    if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
      CoroutineScope(Dispatchers.IO).launch {
        try {
          studentDao.insertStudent(StudentModel(mssv, hoten))
          loadStudents()

          withContext(Dispatchers.Main) {
            _studentAdapter.notifyDataSetChanged()
            supportFragmentManager.popBackStack()
            Snackbar.make(
              findViewById(android.R.id.content),
              "Added new student",
              Snackbar.LENGTH_LONG
            ).show()
          }
        } catch (e: Exception) {
          withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Database Error!", Toast.LENGTH_SHORT).show()
          }
          e.printStackTrace()
        }
      }
    }
  }

  override fun getListStudent(): MutableList<StudentModel> {
    return _students
  }

  override fun setStudentAdapter(studentAdapter: StudentAdapter) {
    _studentAdapter = studentAdapter
  }

  override fun onCancelEdit() {
    supportFragmentManager.popBackStack()
  }

  override fun onEditStudent(hoten: String, mssv: String) {
    if (editingStudent == null) {
      Snackbar.make(
        findViewById(android.R.id.content),
        "Invalid student position",
        Snackbar.LENGTH_LONG
      ).show()
      return
    }

    if (hoten.isNotEmpty() && mssv.isNotEmpty()) {
      CoroutineScope(Dispatchers.IO).launch {
        try {
          studentDao.updateStudent(StudentModel(mssv, hoten))
          loadStudents()

          withContext(Dispatchers.Main) {
            _studentAdapter.notifyDataSetChanged()
            supportFragmentManager.popBackStack()
            Snackbar.make(
              findViewById(android.R.id.content),
              "Update successfully",
              Snackbar.LENGTH_LONG
            ).show()
          }
        } catch (e: Exception) {
          withContext(Dispatchers.Main) {
            Toast.makeText(this@MainActivity, "Database Error", Toast.LENGTH_SHORT).show()
          }
          e.printStackTrace()
        }
      }
    }
  }

  private suspend fun loadStudents() {
    _students.clear()
    val studentEntities = studentDao.getAllStudents()
    _students.addAll(studentEntities.map { StudentModel(it.studentId, it.studentName) })
  }
}