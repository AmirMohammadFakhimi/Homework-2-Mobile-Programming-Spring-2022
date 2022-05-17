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

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ForecastRecyclerViewAdaptor extends RecyclerView.Adapter<ForecastRecyclerViewAdaptor.ViewHolder> {
    private final ArrayList<Weather> weathers;
    private final LayoutInflater inflater;
    private ItemClickListener clickListener;
    private final Context context;

    public ForecastRecyclerViewAdaptor(Context context, ArrayList<Weather> weathers) {
        this.inflater = LayoutInflater.from(context);
        this.weathers = weathers;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = inflater.inflate(R.layout.forecast_recycler_view_row, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Weather weather = weathers.get(position);

        String prefix1;
        String prefix2;
        String prefix3;

        if (position == 0) {
            prefix1 = "current temperature:\n";
            prefix2 = "current feels like:\n";
            prefix3 = "description:\n";
        } else {
            prefix1 = "day forecast temperature:\n";
            prefix2 = "day forecast feels like:\n";
            prefix3 = "forecast description:\n";
        }

        Picasso.get().load(context.getResources().getIdentifier("ic_" + weather.getIcon(), "drawable", context.getPackageName()))
                .into(holder.icon);
        holder.temp.setText(prefix1 + weather.getTemperature());
        holder.feelsLike.setText(prefix2 + weather.getFeelsLike());
        holder.description.setText(prefix3 + weather.getDescription());
    }

    @Override
    public int getItemCount() {
        return weathers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView icon;
        TextView temp;
        TextView feelsLike;
        TextView description;

        ViewHolder(@NonNull View itemView) {
            super(itemView);
            temp = itemView.findViewById(R.id.temp);
            feelsLike = itemView.findViewById(R.id.feels_like);
            description = itemView.findViewById(R.id.description);
            icon = itemView.findViewById(R.id.imageView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (clickListener != null) clickListener.onItemClick(view, getAdapterPosition());
        }
    }

    public Weather getItem(int id) {
        return weathers.get(id);
    }

    public void setClickListener(ItemClickListener itemClickListener) {
        this.clickListener = itemClickListener;
    }

    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void clear() {
        weathers.clear();
        notifyDataSetChanged();
    }
}
