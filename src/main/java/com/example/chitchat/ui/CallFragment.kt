package com.example.chitchat.ui

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.chitchat.R
import com.example.chitchat.databinding.FragmentCallBinding
import com.example.chitchat.databinding.FragmentChatBinding

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [CallFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class CallFragment : Fragment() {
    // TODO: Rename and change types of parameters

    lateinit var binding: FragmentCallBinding




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment

        binding=FragmentCallBinding.inflate(layoutInflater)

        binding.btnStartCall.setOnClickListener {
            val intent=Intent(requireContext(),CallingScreen::class.java)
            startActivity(intent)
        }

        return binding.root
    }




}