package nanodegree.udacity.vk.com.udacitynanodegree.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import butterknife.Bind;
import butterknife.ButterKnife;
import nanodegree.udacity.vk.com.udacitynanodegree.R;
import nanodegree.udacity.vk.com.udacitynanodegree.adapters.PortfolioListAdapter;

public class MainActivity extends BaseActivity {

    @Bind(R.id.portfolioView)
    RecyclerView portfolioView;

    protected RecyclerView.LayoutManager listLayoutManager;
    private PortfolioListAdapter portfolioListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setTitle(getString(R.string.app_name));

        listLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        portfolioView.setLayoutManager(listLayoutManager);
        portfolioListAdapter = new PortfolioListAdapter(this);
        portfolioView.setAdapter(portfolioListAdapter);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_about) {
            startActivity(new Intent(this, AboutActivity.class));
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
