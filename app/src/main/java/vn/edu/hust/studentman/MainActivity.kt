package vn.edu.hust.studentman

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.ContextMenu
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.AdapterView.AdapterContextMenuInfo
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity(),
  AddStudentFragment.IAddStudentInteraction,
  ListStudentFragment.IListStudentInteraction,
  EditStudentFragment.IEditStudentInteraction {

  private lateinit var _students: MutableList<StudentModel>;
  private lateinit var _studentAdapter: StudentAdapter;

  private var deletedStudent: StudentModel? = null;
  private var editingStudent: StudentModel? = null;
  private var deletedPosition: Int = -1;

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    _students = mutableListOf(
      StudentModel("Nguyễn Văn An", "SV001"),
      StudentModel("Trần Thị Bảo", "SV002"),
      StudentModel("Lê Hoàng Cường", "SV003"),
      StudentModel("Phạm Thị Dung", "SV004"),
      StudentModel("Đỗ Minh Đức", "SV005"),
      StudentModel("Vũ Thị Hoa", "SV006"),
      StudentModel("Hoàng Văn Hải", "SV007"),
      StudentModel("Bùi Thị Hạnh", "SV008"),
      StudentModel("Đinh Văn Hùng", "SV009"),
      StudentModel("Nguyễn Thị Linh", "SV010"),
      StudentModel("Phạm Văn Long", "SV011"),
      StudentModel("Trần Thị Mai", "SV012"),
      StudentModel("Lê Thị Ngọc", "SV013"),
      StudentModel("Vũ Văn Nam", "SV014"),
      StudentModel("Hoàng Thị Phương", "SV015"),
      StudentModel("Đỗ Văn Quân", "SV016"),
      StudentModel("Nguyễn Thị Thu", "SV017"),
      StudentModel("Trần Văn Tài", "SV018"),
      StudentModel("Phạm Thị Tuyết", "SV019"),
      StudentModel("Lê Văn Vũ", "SV020")
    )
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

        _students.removeAt(position)
        _studentAdapter.notifyDataSetChanged()

        val rootView = findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "Deleted successfully", Snackbar.LENGTH_LONG)
            .setAction("Undo") {
                if (deletedStudent !== null && deletedPosition != -1) {
                    _students.add(deletedPosition, deletedStudent!!)
                    _studentAdapter.notifyDataSetChanged()

                    deletedStudent = null;
                    deletedPosition = -1;

                    Snackbar.make(rootView, "Undo successfully", Snackbar.LENGTH_LONG).show()
                }
            }
            .show()
      }
      .setNegativeButton("Cancel", null)
      .show()
  }


  override fun onCancelAdd() {
    supportFragmentManager.popBackStack();
  }

  override fun onAddStudent(hoten: String, mssv: String) {
    _students.add(StudentModel(hoten, mssv));
    supportFragmentManager.popBackStack();
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
    Log.v("testtttt", ">>>>>>>${hoten} - ${mssv}")
    if (editingStudent == null) {
      Snackbar.make(findViewById(android.R.id.content), "Invalid student position", Snackbar.LENGTH_LONG).show()
      return
    } else {
      if (hoten != null) {
        editingStudent!!.studentName = hoten
      }

      if (mssv != null) {
        editingStudent!!.studentId = mssv
      }
    }

    supportFragmentManager.popBackStack();
  }
}