package com.example.filemanager

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.File

class FileListFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: FileAdapter
    private var currentPath: File = Environment.getExternalStorageDirectory()

    companion object {
        fun newInstance() = FileListFragment()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_file_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        recyclerView = view.findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(context)
        checkPermissions()
        loadFiles()
    }

    private fun checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R)
            if (!Environment.isExternalStorageManager()) {
                val intent = Intent(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                startActivity(intent)
            }
    }

    private fun loadFiles() {
        val files = currentPath.listFiles()
        adapter = FileAdapter(files?.toList() ?: emptyList()) { file ->
            when {
                file.isDirectory -> {
                    currentPath = file
                    loadFiles()
                }
                file.extension.lowercase() in listOf("txt", "log", "json", "xml", "csv") -> {
                    parentFragmentManager.beginTransaction()
                        .replace(R.id.container, TextViewerFragment.newInstance(file.absolutePath))
                        .addToBackStack(null)
                        .commit()
                }
                else -> {
                    Toast.makeText(requireContext(), "Unsupported file type", Toast.LENGTH_SHORT).show()
                }
            }
        }
        recyclerView.adapter = adapter

        // Add TextView for empty state
        val emptyText = view?.findViewById<TextView>(R.id.emptyText)
        emptyText?.visibility = if (files.isNullOrEmpty()) View.VISIBLE else View.GONE
    }

    fun handleBackPress(): Boolean {
        return if (currentPath != Environment.getExternalStorageDirectory()) {
            currentPath = currentPath.parentFile!!
            loadFiles()
            true
        } else {
            false
        }
    }
}