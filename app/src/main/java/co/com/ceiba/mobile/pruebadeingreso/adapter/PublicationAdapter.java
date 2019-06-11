package co.com.ceiba.mobile.pruebadeingreso.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.entity.Publication;

public class PublicationAdapter extends RecyclerView.Adapter<PublicationAdapter.PublicationViewHolder> {
    private List<Publication> publications;

    class PublicationViewHolder extends RecyclerView.ViewHolder {
        TextView title, body;

        PublicationViewHolder(View itemView) {
            super(itemView);

            title = itemView.findViewById(R.id.title);
            body = itemView.findViewById(R.id.body);
        }
    }

    public PublicationAdapter(List<Publication> users) {
        this.publications = users;
    }

    @Override
    public PublicationAdapter.PublicationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.post_list_item, parent, false);
        return new PublicationAdapter.PublicationViewHolder(v);
    }

    @Override
    public void onBindViewHolder(PublicationAdapter.PublicationViewHolder holder, int position) {
        holder.title.setText(publications.get(position).getTitle());
        holder.body.setText(publications.get(position).getBody());
    }

    @Override
    public int getItemCount() {
        return publications.size();
    }
}
