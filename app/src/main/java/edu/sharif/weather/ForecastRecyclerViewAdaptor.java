package edu.sharif.weather;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class ForecastRecyclerViewAdaptor extends RecyclerView.Adapter<ForecastRecyclerViewAdaptor.ViewHolder> {
    private final ArrayList<Weather> data;
    private final LayoutInflater inflater;
    private ItemClickListener clickListener;

    public ForecastRecyclerViewAdaptor(Context context, ArrayList<Weather> data) {
        this.inflater = LayoutInflater.from(context);
        this.data = data;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.forecast_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        Weather test_string = data.get(position);
        holder.listText.setText(test_string.toString());
        holder.listImage.setImageResource(R.drawable.ic_settings);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView listText;
        ImageView listImage;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            listText = itemView.findViewById(R.id.list_text);
            listImage = itemView.findViewById(R.id.list_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Weather getItem(int id) {
        return data.get(id);
    }

    void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        data.clear();
        notifyDataSetChanged();
    }
}
