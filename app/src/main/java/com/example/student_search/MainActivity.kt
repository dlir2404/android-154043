package com.example.student_search

import android.os.Bundle
import android.widget.EditText
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.widget.doAfterTextChanged
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.student_search.ui.theme.Student_searchTheme
import kotlin.random.Random

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_layout);

        val students = mutableListOf<Student>();
        students.add(Student("Vu Dinh Linh", "20215415"));
        students.add(Student("Nguyen Van A", "20215416"))
        students.add(Student("Tran Thi B", "20215417"))
        students.add(Student("Le Van C", "20215418"))
        students.add(Student("Pham Thi D", "20215419"))
        students.add(Student("Hoang Van E", "20215420"))
        students.add(Student("Ngo Thi F", "20215421"))
        students.add(Student("Do Van G", "20215422"))
        students.add(Student("Bui Thi H", "20215423"))
        students.add(Student("Duong Van I", "20215424"))
        students.add(Student("Ly Thi J", "20215425"))
        students.add(Student("Nguyen Thi K", "20215426"))
        students.add(Student("Tran Van L", "20215427"))
        students.add(Student("Le Thi M", "20215428"))
        students.add(Student("Pham Van N", "20215429"))
        students.add(Student("Hoang Thi O", "20215430"))
        students.add(Student("Ngo Van P", "20215431"))
        students.add(Student("Do Thi Q", "20215432"))
        students.add(Student("Bui Van R", "20215433"))
        students.add(Student("Duong Thi S", "20215434"))
        students.add(Student("Ly Van T", "20215435"))
        students.add(Student("Nguyen Van U", "20215436"))
        students.add(Student("Tran Thi V", "20215437"))
        students.add(Student("Le Van W", "20215438"))
        students.add(Student("Pham Thi X", "20215439"))
        students.add(Student("Hoang Van Y", "20215440"))
        students.add(Student("Ngo Thi Z", "20215441"))
        students.add(Student("Do Van AA", "20215442"))
        students.add(Student("Bui Thi BB", "20215443"))
        students.add(Student("Duong Van CC", "20215444"))
        students.add(Student("Ly Thi DD", "20215445"))
        students.add(Student("Nguyen Thi EE", "20215446"))
        students.add(Student("Tran Van FF", "20215447"))
        students.add(Student("Le Thi GG", "20215448"))
        students.add(Student("Pham Van HH", "20215449"))
        students.add(Student("Hoang Thi II", "20215450"))
        students.add(Student("Ngo Van JJ", "20215451"))
        students.add(Student("Do Thi KK", "20215452"))
        students.add(Student("Bui Van LL", "20215453"))
        students.add(Student("Duong Thi MM", "20215454"))
        students.add(Student("Ly Van NN", "20215455"))
        students.add(Student("Nguyen Van OO", "20215456"))
        students.add(Student("Tran Thi PP", "20215457"))
        students.add(Student("Le Van QQ", "20215458"))
        students.add(Student("Pham Thi RR", "20215459"))
        students.add(Student("Hoang Van SS", "20215460"))
        students.add(Student("Ngo Thi TT", "20215461"))
        students.add(Student("Do Van UU", "20215462"))
        students.add(Student("Bui Thi VV", "20215463"))
        students.add(Student("Duong Van WW", "20215464"))
        students.add(Student("Ly Thi XX", "20215465"))
        students.add(Student("Nguyen Thi YY", "20215466"))
        students.add(Student("Tran Van ZZ", "20215467"))

        val adapter = StudentAdapter(students);

        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view);
        recyclerView.layoutManager = LinearLayoutManager(this);
        recyclerView.adapter = adapter

        val input = findViewById<EditText>(R.id.input);
        input.doAfterTextChanged { text ->
            if (text != null && text.length > 2) {
                val filteredStudents = students.filter { student ->
                    student.hoTen.contains(text.toString(), ignoreCase = true) ||
                    student.mssv.contains(text.toString(), ignoreCase = true)
                }
                adapter.updateData(filteredStudents)
            } else {
                adapter.updateData(students)
            }
        }
    }
}