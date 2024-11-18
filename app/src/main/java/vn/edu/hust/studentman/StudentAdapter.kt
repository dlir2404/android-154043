package vn.edu.hust.studentman

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class StudentAdapter(
  val students: MutableList<StudentModel>,
  val context: Context
): RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {
  private var deletedStudent: StudentModel? = null;
  private var deletedPosition: Int = -1;
  class StudentViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    val textStudentName: TextView = itemView.findViewById(R.id.text_student_name)
    val textStudentId: TextView = itemView.findViewById(R.id.text_student_id)
    val imageEdit: ImageView = itemView.findViewById(R.id.image_edit)
    val imageRemove: ImageView = itemView.findViewById(R.id.image_remove)
  }

  override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentViewHolder {
    val itemView = LayoutInflater.from(parent.context).inflate(R.layout.layout_student_item,
       parent, false)
    return StudentViewHolder(itemView)
  }

  override fun getItemCount(): Int = students.size

  override fun onBindViewHolder(holder: StudentViewHolder, position: Int) {
    val student = students[position]

    holder.textStudentName.text = student.studentName
    holder.textStudentId.text = student.studentId
    holder.imageEdit.setOnClickListener{
      showEditDialog(student, position)
    }
    holder.imageRemove.setOnClickListener {
      showDeleteDialog(student, position, holder)
    }
  }

  private fun showEditDialog(student: StudentModel, position: Int) {
    val editDialog = Dialog(context)
    editDialog.setContentView(R.layout.edit_dialog)
    editDialog.window?.setLayout(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.WRAP_CONTENT)

    val editHoten = editDialog.findViewById<EditText>(R.id.edit_hoten)
    val editMssv = editDialog.findViewById<EditText>(R.id.edit_mssv)
    val btnOk = editDialog.findViewById<Button>(R.id.button_ok)
    val btnCancel = editDialog.findViewById<Button>(R.id.button_cancel)

    editHoten.setText(student.studentName)
    editMssv.setText(student.studentId)

    btnOk.setOnClickListener {
      student.studentName = editHoten.text.toString()
      student.studentId = editMssv.text.toString()

      notifyItemChanged(position)

      editDialog.dismiss()
    }

    btnCancel.setOnClickListener {
      editDialog.dismiss()
    }

    editDialog.show()
  }

  private fun showDeleteDialog(student: StudentModel, position: Int, holder: StudentViewHolder) {
    AlertDialog.Builder(context)
      .setTitle("Delete student?")
      .setPositiveButton("OK") { _, _ ->
        deletedStudent = student
        deletedPosition = position

        students.removeAt(position)
        notifyItemRemoved(position)

        val rootView = (context as Activity).findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "Deleted successfully", Snackbar.LENGTH_LONG)
          .setAction("Undo") {
            if (deletedStudent !== null && deletedPosition != -1) {
              students.add(deletedPosition, deletedStudent!!)
              notifyItemInserted(deletedPosition)

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
}