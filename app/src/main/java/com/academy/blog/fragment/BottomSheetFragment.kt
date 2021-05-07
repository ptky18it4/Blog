package com.academy.blog.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.academy.blog.R
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth


class BottomSheetFragment : BottomSheetDialogFragment() {
    private lateinit var mAuth: FirebaseAuth
    override fun getTheme(): Int = R.style.BottomSheet
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val v: View = inflater.inflate(R.layout.bottom_sheet_profile, container, false)
        val btnSignOut = v.findViewById<Button>(R.id.LogOut)
        mAuth = FirebaseAuth.getInstance()
        btnSignOut.setOnClickListener {
            mAuth.signOut()
            /*val intent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(intent)
            finish()*/
        }
        return v
    }


}