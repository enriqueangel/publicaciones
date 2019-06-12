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

public class UserAdapter extends RecyclerView.Adapter<BaseViewHolder> {
    private List<User> users;
    public static final int VIEW_TYPE_EMPTY = 0;
    public static final int VIEW_TYPE_NORMAL = 1;

    class UserViewHolder extends BaseViewHolder {
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

        public void onBind(int position){
            super.onBind(position);

            name.setText(users.get(position).getName());
            email.setText(users.get(position).getEmail());
            phone.setText(users.get(position).getPhone());
            id = users.get(position).getId();
        }

        @Override
        protected void clear() {}
    }

    public class UserEmptyViewHolder extends BaseViewHolder {

        UserEmptyViewHolder(View itemView) {
            super(itemView);
        }

        @Override
        protected void clear() {}
    }

    public UserAdapter(List<User> users) {
        this.users = users;
    }

    @Override
    public BaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case VIEW_TYPE_NORMAL:
                return new UserViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false));
            case VIEW_TYPE_EMPTY:
            default:
                return new UserEmptyViewHolder(
                        LayoutInflater.from(parent.getContext()).inflate(R.layout.empty_view, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(BaseViewHolder holder, int position) {
        holder.onBind(position);
    }

    @Override
    public int getItemCount() {
        if (users != null && users.size() > 0) {
            return users.size();
        } else {
            return 1;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (users != null && users.size() > 0) {
            return VIEW_TYPE_NORMAL;
        } else {
            return VIEW_TYPE_EMPTY;
        }
    }

    public void setFilter(ArrayList<User> list) {
        this.users = new ArrayList<>();
        this.users.addAll(list);
        notifyDataSetChanged();
    }
}
