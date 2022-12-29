package nyp.sit.movieviewer.basic.data

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.TextView
import nyp.sit.movieviewer.basic.R
import nyp.sit.movieviewer.basic.entity.SimpleMovieItem

class DatabaseAdapter(
    context: Context,
    private val resource: Int = R.layout.simpleitem,
    objects: MutableList<SimpleMovieItem>
):
    ArrayAdapter<SimpleMovieItem>(context,resource,objects){
    private val mContext: Context

    init {
        mContext = context
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        var view = convertView
        if (view == null) {
            val viewInflater: LayoutInflater = LayoutInflater.from(mContext)
            view = viewInflater.inflate(resource, null)
        }
        val movie: SimpleMovieItem? = getItem(position)

        if (movie != null) {
            val name: TextView = view!!.findViewById(R.id.movie_name)
            name.text = movie.title
        }
        return view!!
    }

}