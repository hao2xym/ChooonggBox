package chooongg.box.core.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import chooongg.box.core.fragment.BoxFragment

open class FragmentAdapter<T : BoxFragment> : FragmentStateAdapter {

    val fragments: ArrayList<T>

    constructor(fragment: Fragment, fragments: ArrayList<T>) : super(fragment) {
        this.fragments = fragments
    }

    constructor(activity: FragmentActivity, fragments: ArrayList<T>) : super(activity) {
        this.fragments = fragments
    }

    constructor(
        fragmentManager: FragmentManager,
        lifecycle: Lifecycle,
        fragments: ArrayList<T>
    ) : super(fragmentManager, lifecycle) {
        this.fragments = fragments
    }

    override fun getItemCount() = fragments.size
    override fun createFragment(position: Int) = fragments[position]

    open fun getTitle(position: Int) = fragments[position].getTitle()
}