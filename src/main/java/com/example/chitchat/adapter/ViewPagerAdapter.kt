package com.example.chitchat.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter

class ViewPagerAdapter( private val context:Context,fm:FragmentManager?,val list:ArrayList<Fragment>
): FragmentPagerAdapter(fm!!) {
    override fun getCount(): Int {
        return 3
    }

    //A ViewPager is a common UI component in Android used to display multiple pages of content that users can swipe horizontally.

    override fun getItem(position: Int): Fragment {
        return list[position]
    }

    //iss ham page kya title hoga wow return kara ga

    override fun getPageTitle(position: Int): CharSequence? {
        return TAB_TITLES[position]
    }

    //it is used to create a global variable in the kotlin


    companion object{
        val TAB_TITLES= arrayOf("Chats","Status","Call")
    }
}