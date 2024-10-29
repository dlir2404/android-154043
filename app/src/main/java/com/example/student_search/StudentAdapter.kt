package com.example.student_search

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class StudentAdapter(val students: List<Student>) : RecyclerView.Adapter<StudentAdapter.StudentViewHolder>() {

    private val displayedStudents: MutableList<Student> = students.toMutableList();

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentAdapter.StudentViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_layout, parent, false);
        return StudentViewHolder(itemView);
    }

    override fun onBindViewHolder(holder: StudentAdapter.StudentViewHolder, position: Int) {
        val student = displayedStudents[position]
        holder.hoTen.text = student.hoTen;
        holder.mssv.text = student.mssv;
    }

    override fun getItemCount(): Int = displayedStudents.size;

    fun updateData(newList: List<Student>) {
        displayedStudents.clear()
        displayedStudents.addAll(newList)
        notifyDataSetChanged()
    }

    class StudentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hoTen: TextView;
        val mssv: TextView;

        init {
            hoTen = itemView.findViewById<TextView>(R.id.ho_ten);
            mssv = itemView.findViewById<TextView>(R.id.mssv);
        }
    }
}