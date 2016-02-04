package nanodegree.udacity.vk.com.udacitynanodegree.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import nanodegree.udacity.vk.com.udacitynanodegree.R;

/**
 * Created by cropin on 3/2/16.
 */
public class PortfolioListAdapter extends RecyclerView.Adapter<PortfolioListAdapter.PortfolioViewHolder> {

    private Context context;
    private List<String> projectTitles;
    private List<String> projectDescriptions;

    public PortfolioListAdapter(Context c) {
        context = c;
        projectTitles = Arrays.asList(c.getResources().getStringArray(R.array.projectTitles));
        projectDescriptions = Arrays.asList(c.getResources().getStringArray(R.array.projectDescription));
    }

    @Override
    public PortfolioViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        CardView view = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.portfolio_list_item, parent, false);
        PortfolioViewHolder pvh = new PortfolioViewHolder(view);
        return pvh;
    }

    @Override
    public void onBindViewHolder(PortfolioViewHolder holder, int position) {
        int projectNumber = position + 1;
        holder.projectNumber.setText(context.getString(R.string.project) + projectNumber);
        holder.projectName.setText(projectTitles.get(position));
        holder.projectDescription.setText(projectDescriptions.get(position));
    }

    @Override
    public int getItemCount() {
        return projectTitles.size();
    }

    public class PortfolioViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.projectNumber) TextView projectNumber;
        @Bind(R.id.projectName) TextView projectName;
        @Bind(R.id.projectDescription) TextView projectDescription;

        public PortfolioViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            Toast.makeText(context, projectTitles.get(getAdapterPosition()) + context.getString(R.string.projectToast), Toast.LENGTH_SHORT).show();
        }
    }
}
