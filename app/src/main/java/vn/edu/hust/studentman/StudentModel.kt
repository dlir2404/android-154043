package vn.edu.hust.studentman

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "students")
data class StudentModel(
    @PrimaryKey var studentId: String,
    @ColumnInfo(name = "student_name") var studentName: String
)
