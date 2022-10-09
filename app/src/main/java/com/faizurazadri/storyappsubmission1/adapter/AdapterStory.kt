package com.faizurazadri.storyappsubmission1.adapter

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.data.source.response.ListStoryItem
import com.faizurazadri.storyappsubmission1.databinding.ItemStoryBinding
import com.faizurazadri.storyappsubmission1.ui.DetailStoriesActivity

class AdapterStory : RecyclerView.Adapter<AdapterStory.StoriesViewHolder>() {

    private var listStory = ArrayList<ListStoryItem>()

    fun setStories(stories: List<ListStoryItem>?) {
        if (stories == null) return
        this.listStory.clear()
        this.listStory.addAll(stories)
    }

    class StoriesViewHolder(private val binding: ItemStoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(stories: ListStoryItem) {
            with(binding) {
                tvItemName.text = stories.name
                tvItemDescripton.text = stories.description
                Glide.with(itemView.context)
                    .load(stories.photoUrl)
                    .apply(
                        RequestOptions.placeholderOf(R.drawable.no_image).error(R.drawable.no_image)
                    ).into(ivItemPhoto)

                itemView.setOnClickListener {
                    val optionsCompat: ActivityOptionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation(
                        itemView.context as Activity,
                        Pair(binding.ivItemPhoto, "story_image"),
                        Pair(binding.tvItemName, "username"),
                        Pair(binding.tvItemDescripton, "description")
                    )
                    val intent = Intent(itemView.context, DetailStoriesActivity::class.java)
                    intent.putExtra(DetailStoriesActivity.EXTRA_NAME, stories.name)
                    intent.putExtra(DetailStoriesActivity.EXTRA_IMAGE, stories.photoUrl)
                    intent.putExtra(DetailStoriesActivity.EXTRA_DESCRIPTION, stories.description)
                    itemView.context.startActivity(
                        intent,
                        optionsCompat.toBundle()
                    )
                }
            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val itemStoryBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val stories = listStory[position]
        holder.bind(stories)
    }

    override fun getItemCount(): Int = listStory.size
}