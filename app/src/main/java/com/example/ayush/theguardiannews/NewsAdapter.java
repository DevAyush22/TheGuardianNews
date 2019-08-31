package com.example.ayush.theguardiannews;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * A {@link NewsAdapter} knows how to create a list item layout for each news article
 * in the data source (a list of {@link News} objects.
 * <p>
 * These list item layouts will be provided to an adapter view like RecyclerView
 * to be displayed to the user.
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.MyViewHolder> {

    private Context context;
    private List<News> newsList;

    /**
     * ViewHolder class to hold exact set of views
     */
    static class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView title;
        private TextView section;
        private TextView date;
        private TextView time;
        private TextView author;
        private View parentView;

        /**
         * Represents the views of RecyclerView.
         *
         * @param view is the object wherein the views are.
         */
        private MyViewHolder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.article_title);
            this.section = (TextView) view.findViewById(R.id.section_name);
            this.date = (TextView) view.findViewById(R.id.date);
            this.time = (TextView) view.findViewById(R.id.time);
            this.author = (TextView) view.findViewById(R.id.author_name);
            this.parentView = view;
        }

    }

    /**
     * @param context  holds the application resources
     * @param newsList is the list of the news object
     */
    public NewsAdapter(Context context, List<News> newsList) {
        this.context = context;
        this.newsList = newsList;
    }

    /**
     * @param parent   is the viewGroup which the viewHolder will inflate
     * @param viewType is the layout
     * @return the view
     */
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.news_list_item, parent, false);
        return new MyViewHolder(itemView);
    }

    /**
     * This method binds the data to the viewHolder.
     *
     * @param holder   handle the views.
     * @param position is the position in the recycler View.
     */
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        // Find the current news that was clicked on
        final News news = newsList.get(position);

        // Set title name to display
        holder.title.setText(news.getTitle());

        // Set section name to display
        holder.section.setText(news.getSection());

        String date = news.getPublicationDate();

        String newsDate = formatDate(date);
        // Set date to display
        holder.date.setText(newsDate);
        String newsTime = formatTime(date);
        // Set time to display
        holder.time.setText(newsTime);

        // Set author's name to display
        String noAuthor = (context.getString(R.string.by_anonymous));
        if (news.getAuthor().length() > 1) {
            holder.author.setText(news.getAuthor());
        } else {
            holder.author.setText(noAuthor);
        }

        // Set an on click listener on that view
        holder.parentView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Convert the String URL into a URI object (to pass into the Intent constructor)
                Uri newsUri = Uri.parse(news.getUrl());

                // Create a new intent to view the news URI
                Intent websiteIntent = new Intent(Intent.ACTION_VIEW, newsUri);

                // Send the intent to launch a new activity
                context.startActivity(websiteIntent);

            }
        });

    }

    // Clear the adapter of previous news data
    public void clear() {
        newsList.clear();
        notifyDataSetChanged();
    }

    // If there is a valid list of {@link New}s, then add them to the adapter's
    // data set. This will trigger the RecyclerView to update.
    public void addAll(List<News> news) {
        newsList.addAll(news);
        notifyItemInserted(newsList.size() - 1);
    }

    /**
     * This returns the size of the list.
     *
     * @return the size of the array
     */
    @Override
    public int getItemCount() {
        return newsList.size();
    }

    /**
     * This method format the date into a specific pattern.
     *
     * @param dateObj is the web publication date.
     * @return a date formatted into string.
     */
    private String formatDate(String dateObj) {
        String formattedDate = "";
        SimpleDateFormat inputDate = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputDate = new SimpleDateFormat("LLL dd, yyyy", Locale.getDefault());
        try {
            Date newDate = inputDate.parse(dateObj);
            return outputDate.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedDate;
    }

    /**
     * This method format the time into a specific pattern.
     *
     * @param dateObj is the web publication date.
     * @return time formatted into string.
     */
    private String formatTime(String dateObj) {
        String formattedTime = "";
        SimpleDateFormat inputTime = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.getDefault());
        SimpleDateFormat outputTime = new SimpleDateFormat("h:mm a", Locale.getDefault());
        try {
            Date newDate = inputTime.parse(dateObj);
            return outputTime.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return formattedTime;
    }

}
