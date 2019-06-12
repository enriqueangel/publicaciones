package co.com.ceiba.mobile.pruebadeingreso.adapter;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.data.User;
import co.com.ceiba.mobile.pruebadeingreso.view.PostActivity;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {
    private List<User> users;

    class UserViewHolder extends RecyclerView.ViewHolder {
        TextView name, phone, email;
        Button publicationsBtn;
        int id;

        UserViewHolder(View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.name);
            phone = itemView.findViewById(R.id.phone);
            email = itemView.findViewById(R.id.email);
            publicationsBtn = itemView.findViewById(R.id.btn_view_post);

            publicationsBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(v.getContext(), PostActivity.class);
                    intent.putExtra("user", String.valueOf(id));
                    v.getContext().startActivity(intent);
                }
            });
        }
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public UserViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.user_list_item, parent, false);
        return new UserAdapter.UserViewHolder(v);
    }

    @Override
    public void onBindViewHolder(UserViewHolder holder, int position) {
        holder.name.setText(users.get(position).getName());
        holder.email.setText(users.get(position).getEmail());
        holder.phone.setText(users.get(position).getPhone());
        holder.id = users.get(position).getId();
    }

    @Override
    public int getItemCount() {
        return users.size();
    }

    public void setFilter(ArrayList<User> list) {
        this.users = new ArrayList<>();
        this.users.addAll(list);
        notifyDataSetChanged();
    }
}
