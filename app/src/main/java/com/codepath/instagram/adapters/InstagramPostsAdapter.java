package com.codepath.instagram.adapters;

import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.text.style.ForegroundColorSpan;
import android.text.style.TypefaceSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.codepath.instagram.R;
import com.codepath.instagram.core.MainApplication;
import com.codepath.instagram.helpers.Utils;
import com.codepath.instagram.models.InstagramPost;
import com.facebook.drawee.view.SimpleDraweeView;

import java.util.ArrayList;
import java.util.List;

public class InstagramPostsAdapter extends RecyclerView.Adapter<InstagramPostsAdapter.PostItemViewHolder> {
    private static final String TAG = "InstagramPostsAdapter";
    private List<InstagramPost> posts;

    public InstagramPostsAdapter(List<InstagramPost> posts) {
        this.posts = (posts == null ? new ArrayList<InstagramPost>() : posts);
    }

    @Override
    public PostItemViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_post, viewGroup, false);
        return new PostItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(PostItemViewHolder postItemViewHolder, int position) {
        InstagramPost instagramPost = posts.get(position);

        if (instagramPost.caption != null) {
            SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(instagramPost.user.userName);
            ForegroundColorSpan foregroundColorSpan = new ForegroundColorSpan(MainApplication.sharedApplication().getResources().getColor(R.color.blue_text));
            TypefaceSpan typefaceSpan = new TypefaceSpan("sans-serif-medium");
            spannableStringBuilder.setSpan(typefaceSpan, 0, spannableStringBuilder.length(), Spanned.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.setSpan(foregroundColorSpan, 0, spannableStringBuilder.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            spannableStringBuilder.append(" ");
            spannableStringBuilder.append(instagramPost.caption);
            postItemViewHolder.tvCaption.setText(spannableStringBuilder);
        }

        postItemViewHolder.tvCaption.setVisibility(
                TextUtils.isEmpty(instagramPost.caption) ? View.GONE : View.VISIBLE);

        postItemViewHolder.tvUserName.setText(instagramPost.user.userName);
        postItemViewHolder.tvRelativeTimestamp.setText(
                DateUtils.getRelativeTimeSpanString(instagramPost.createdTime * 1000,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS));

        int likesCount = instagramPost.likesCount;
        postItemViewHolder.tvLikes.setText(Utils.formatNumberForDisplay(likesCount) +
                " like" + (likesCount != 1 ? "s" : ""));
        postItemViewHolder.tvLikes.setVisibility(likesCount == 0 ? View.GONE : View.VISIBLE);

        // Reset image views
        postItemViewHolder.sdvPhoto.setImageURI(null);
        postItemViewHolder.sdvProfileImage.setImageURI(null);


        int width = instagramPost.image.imageWidth;
        int height = instagramPost.image.imageHeight;

        float aspectRatio = height > 0 ? (float)width / (float)height : 1;

        Uri imageUri = Uri.parse(instagramPost.image.imageUrl);
        postItemViewHolder.sdvPhoto.setImageURI(imageUri);
        postItemViewHolder.sdvPhoto.setAspectRatio(aspectRatio);

        Uri profilePictureUri = Uri.parse(instagramPost.user.profilePictureUrl);
        postItemViewHolder.sdvProfileImage.setImageURI(profilePictureUri);
    }

    @Override
    public int getItemCount() {
        return (posts == null ? 0 : posts.size());
    }

    public static final class PostItemViewHolder extends RecyclerView.ViewHolder {
        TextView tvUserName;
        TextView tvRelativeTimestamp;

        SimpleDraweeView sdvProfileImage;
        SimpleDraweeView sdvPhoto;

        TextView tvCaption;
        TextView tvLikes;

        public PostItemViewHolder(View itemView) {
            super(itemView);

            tvUserName = (TextView) itemView.findViewById(R.id.tvUserName);
            tvRelativeTimestamp = (TextView) itemView.findViewById(R.id.tvRelativeTimestamp);

            sdvProfileImage = (SimpleDraweeView) itemView.findViewById(R.id.sdvProfileImage);
            sdvPhoto = (SimpleDraweeView) itemView.findViewById(R.id.sdvPhoto);

            tvCaption = (TextView) itemView.findViewById(R.id.tvCaption);
            tvLikes = (TextView) itemView.findViewById(R.id.tvLikes);
        }
    }
}
