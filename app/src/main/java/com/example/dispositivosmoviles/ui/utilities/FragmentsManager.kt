package com.example.dispositivosmoviles.ui.utilities

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
class FragmentsManager {
    fun addFragment(
        manager : FragmentManager,
        container : Int,
        fragment: Fragment
    ){
        with(manager.beginTransaction()){
            replace(container, fragment);
            commit();
        }
    }

    fun replaceFragment(
        manager : FragmentManager,
        container : Int,
        fragment: Fragment
    ){
        with(manager.beginTransaction()){
            replace(container, fragment);
            addToBackStack(null);
            commit();
        }
    }

    fun hideFragment(fragmentManager: FragmentManager, fragment: Fragment?) {
        fragment?.let {
            fragmentManager.beginTransaction().hide(it).commit()
        }
    }

    fun showFragment(fragmentManager: FragmentManager, fragment: Fragment?) {
        fragment?.let {
            fragmentManager.beginTransaction().show(it).commit()
        }
    }
}