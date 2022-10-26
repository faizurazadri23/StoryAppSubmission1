package com.faizurazadri.storyappsubmission1.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.app.ActivityOptionsCompat
import androidx.core.util.Pair
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.faizurazadri.storyappsubmission1.R
import com.faizurazadri.storyappsubmission1.data.source.model.ListStoryItem
import com.faizurazadri.storyappsubmission1.databinding.ItemStoryBinding
import com.faizurazadri.storyappsubmission1.ui.DetailStoriesActivity

class AdapterStory :
    PagingDataAdapter<ListStoryItem, AdapterStory.StoriesViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoriesViewHolder {
        val itemStoryBinding =
            ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoriesViewHolder(itemStoryBinding)
    }

    override fun onBindViewHolder(holder: StoriesViewHolder, position: Int) {
        val data = getItem(position)

        if (data != null) {
            holder.bind(data)
        }
    }

    inner class StoriesViewHolder(private val binding: ItemStoryBinding) :
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

                    val intent = Intent(itemView.context, DetailStoriesActivity::class.java)
                    intent.putExtra(DetailStoriesActivity.EXTRA_NAME, stories.name)
                    intent.putExtra(DetailStoriesActivity.EXTRA_IMAGE, stories.photoUrl)
                    intent.putExtra(DetailStoriesActivity.EXTRA_DESCRIPTION, stories.description)

                    val optionsCompat: ActivityOptionsCompat =
                        ActivityOptionsCompat.makeSceneTransitionAnimation(
                            itemView.context as Activity,
                            Pair(ivItemPhoto, "story_image"),
                            Pair(tvItemName, "username"),
                            Pair(tvItemDescripton, "description")
                        )
                    itemView.context.startActivity(
                        intent,
                        optionsCompat.toBundle()
                    )
                }
            }
        }

    }


    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListStoryItem>() {
            override fun areItemsTheSame(oldItem: ListStoryItem, newItem: ListStoryItem): Boolean {
                return oldItem == newItem
            }

            @SuppressLint("DiffUtilEquals")
            override fun areContentsTheSame(
                oldItem: ListStoryItem,
                newItem: ListStoryItem
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}