package com.bogdan.codeforceswatcher.features.problems

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.bogdan.codeforceswatcher.R
import com.bogdan.codeforceswatcher.features.problems.models.Problem
import com.bogdan.codeforceswatcher.features.problems.redux.requests.ProblemsRequests
import com.bogdan.codeforceswatcher.store
import kotlinx.android.synthetic.main.view_problem_item.view.*

class ProblemsAdapter(
    private val context: Context,
    private val itemClickListener: (Int) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var items: List<Problem> = listOf()

    override fun getItemCount() = items.size + if (items.isEmpty()) 1 else 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        when (viewType) {
            STUB_VIEW_TYPE -> {
                val layout = LayoutInflater.from(context).inflate(R.layout.view_action_stub, parent, false)
                StubViewHolder(layout)
            }
            else -> {
                val layout = LayoutInflater.from(context).inflate(R.layout.view_problem_item, parent, false)
                ProblemViewHolder(layout, itemClickListener)
            }
        }

    override fun getItemViewType(position: Int): Int {
        return when {
            items.isEmpty() -> STUB_VIEW_TYPE
            else -> PROBLEM_VIEW_TYPE
        }
    }

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        if (items.isEmpty()) return

        val problemViewHolder = viewHolder as ProblemViewHolder
        val problem = items[position]

        problemViewHolder.problem = problem
        problemViewHolder.tvProblemName.text = problem.name
        problemViewHolder.tvContestName.text = problem.contestName
        if (problem.isFavourite) problemViewHolder.ivFavourite.setColorFilter(ContextCompat.getColor(context, R.color.colorAccent))
        else problemViewHolder.ivFavourite.setColorFilter(ContextCompat.getColor(context, R.color.dark_grey))
    }

    fun setItems(problemsList: List<Problem>) {
        if (problemsList != items) {
            items = problemsList
            notifyDataSetChanged()
        }
    }

    class ProblemViewHolder(view: View, itemClickListener: (Int) -> Unit) : RecyclerView.ViewHolder(view) {
        val tvProblemName: TextView = view.tvProblemName
        val tvContestName: TextView = view.tvContestName
        val ivFavourite: ImageView = view.ivFavourite
        lateinit var problem: Problem

        init {
            view.setOnClickListener {
                itemClickListener.invoke(adapterPosition)
            }
            view.ivFavourite.setOnClickListener {
                store.dispatch(ProblemsRequests.MarkProblemFavorite(problem))
            }
        }
    }

    data class StubViewHolder(val view: View) : RecyclerView.ViewHolder(view)

    companion object {
        const val STUB_VIEW_TYPE = 0
        const val PROBLEM_VIEW_TYPE = 1
    }
}