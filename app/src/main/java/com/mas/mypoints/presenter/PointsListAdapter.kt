package com.mas.mypoints.presenter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mas.mypoints.R
import com.mas.mypoints.data.Point

internal class PointsListAdapter(
    var listPoints: ArrayList<Point>,
    var presenter: ListPointsPresenter
) :
    RecyclerView.Adapter<PointsListAdapter.PointsListViewHolder>() {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): PointsListViewHolder {
        return PointsListViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.item_point, null)
        ).listen { pos, action ->
            when (action) {
                0 -> presenter.goToMap(listPoints, listPoints.get(pos))
                1 -> delItem(pos)
                else -> Log.d("err", "err")
            }
        }
    }

    override fun onBindViewHolder(
        holder: PointsListViewHolder,
        position: Int
    ) {
        holder.bind(listPoints[position])
    }

    override fun getItemCount() = listPoints.size


    fun delItem(pos: Int) {
        listPoints.removeAt(pos)
        notifyDataSetChanged()
    }

    internal class PointsListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val tvDescription = itemView.findViewById<TextView>(R.id.tvDescription)
        private val buttonMap = itemView.findViewById<TextView>(R.id.toMap)
        private val buttonDel = itemView.findViewById<TextView>(R.id.delPoint)

        fun bind(point: Point) {
            tvDescription.text = point.description
        }


    }

    fun <T : RecyclerView.ViewHolder> T.listen(event: (position: Int, action: Int) -> Unit): T {
        val buttonMap = itemView.findViewById<TextView>(R.id.toMap)
        val buttonDel = itemView.findViewById<TextView>(R.id.delPoint)
        buttonMap.setOnClickListener {
            event.invoke(getAdapterPosition(), 0)
        }
        buttonDel.setOnClickListener {
            event.invoke(getAdapterPosition(), 1)
        }
        return this
    }
}
