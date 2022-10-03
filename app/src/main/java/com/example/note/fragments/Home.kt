package com.example.note.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.note.NoteEntity
import com.example.note.NoteViewModel
import com.example.note.R
import com.example.note.adapter.NoteAdapter
import com.example.note.databinding.FragmentHomeBinding
import com.google.android.material.bottomsheet.BottomSheetDialog


class Home : Fragment() {

    lateinit var binding:FragmentHomeBinding
    private val fromBottom : Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.from_bottom) }
    private val toBottom : Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.to_bottom) }
    private val rotateOpen : Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_open) }
    private val rotateClose : Animation by lazy { AnimationUtils.loadAnimation(context,R.anim.rotate_close) }
    val noteViewModel: NoteViewModel by viewModels()

    var click = false
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(layoutInflater)

        noteViewModel.allNote.observe(viewLifecycleOwner, Observer {
            binding.recyclerView.adapter = NoteAdapter(it, this, this)

        })

        binding.extendedFab.setOnClickListener {
            extendedFAbClicked()
        }
        binding.fabAdd.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_add)
        }
        binding.fabDeleteAll.setOnClickListener {
            noteViewModel.deleteAll()
        }
        binding.fabLike.setOnClickListener {
            findNavController().navigate(R.id.action_home_to_favourites)
        }

        return binding.root
    }

    fun onItemClicked(noteEntity: NoteEntity) {
        deleteNote(noteEntity)
    }

    private fun deleteNote(noteEntity: NoteEntity) {
        val bottomSheet = BottomSheetDialog(requireContext(),R.style.style)
        bottomSheet.setContentView(R.layout.fragment_bottom_sheet)
        val yes = bottomSheet.findViewById<TextView>(R.id.yes)
        val no = bottomSheet.findViewById<TextView>(R.id.no)
        yes?.setOnClickListener{
            noteViewModel.delNote(noteEntity)
            Toast.makeText(context, "Deleted Successfully", Toast.LENGTH_SHORT).show()
            bottomSheet.dismiss()
        }
        no?.setOnClickListener {
            bottomSheet.dismiss()
        }
        bottomSheet.show()
    }

    fun onFavItemClicked(idd: Int, like: Boolean, title: String, description: String) {
       val noteEntity = NoteEntity(
           id=0,
           title=title,
           description=description,
           isLike = like)

       noteEntity.id=idd
       noteViewModel.addLike(noteEntity)

    }

    private fun extendedFAbClicked() {
        setAnimation(click)
        setVisibility(click)
        setAnimationOfTitle(click)
        click = !click

    }

    private fun setAnimationOfTitle(click: Boolean) {
        if (!click){
            binding.like.startAnimation(fromBottom)
            binding.add.startAnimation(fromBottom)
            binding.deleteAll.startAnimation(fromBottom)
        }else{
            binding.like.startAnimation(toBottom)
            binding.deleteAll.startAnimation(toBottom)
            binding.add.startAnimation(toBottom)

        }
    }

    private fun setVisibility(click:Boolean){
        if (!click){
            binding.add.visibility=View.VISIBLE
            binding.fabAdd.visibility=View.VISIBLE
            binding.like.visibility=View.VISIBLE
            binding.deleteAll.visibility=View.VISIBLE
            binding.fabLike.visibility=View.VISIBLE
            binding.fabDeleteAll.visibility=View.VISIBLE
        }
        else{
            binding.add.visibility=View.INVISIBLE
            binding.fabAdd.visibility=View.INVISIBLE
            binding.like.visibility=View.INVISIBLE
            binding.deleteAll.visibility=View.INVISIBLE
            binding.fabLike.visibility=View.INVISIBLE
            binding.fabDeleteAll.visibility=View.INVISIBLE
        }
    }

    private fun setAnimation(click: Boolean) {
        if (!click){
            binding.extendedFab.startAnimation(rotateOpen)
            binding.fabLike.startAnimation(fromBottom)
            binding.fabDeleteAll.startAnimation(fromBottom)
            binding.fabAdd.startAnimation(fromBottom)
        }else{
            binding.extendedFab.startAnimation(rotateClose)
            binding.fabLike.startAnimation(toBottom)
            binding.fabDeleteAll.startAnimation(toBottom)
            binding.fabAdd.startAnimation(toBottom)

        }
    }
}