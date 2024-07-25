import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity extends Activity {

    private PackageManager packageManager;
    private ActivityManager activityManager;
    private AppOpsManager appOpsManager;
    private List<ApplicationInfo> installedApps;
    private ListView appListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        packageManager = getPackageManager();
        activityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
        appListView = findViewById(R.id.app_list_view);

        // Get a list of all installed apps
        installedApps = packageManager.getInstalledApplications(PackageManager.GET_META_DATA);

        // Create an adapter to display the app list
        AppListAdapter adapter = new AppListAdapter(this, R.layout.app_list_item, installedApps);
        appListView.setAdapter(adapter);
    }

    private class AppListAdapter extends ArrayAdapter<ApplicationInfo> {

        private final Context context;
        private final List<ApplicationInfo> apps;
        private final int resource;

        public AppListAdapter(Context context, int resource, List<ApplicationInfo> apps) {
            super(context, resource, apps);
            this.context = context;
            this.resource = resource;
            this.apps = apps;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            if (convertView == null) {
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(resource, parent, false);
            }

            ApplicationInfo appInfo = apps.get(position);
            TextView appName = convertView.findViewById(R.id.app_name);
            TextView appPackage = convertView.findViewById(R.id.app_package);

            appName.setText(appInfo.loadLabel(packageManager));
            appPackage.setText(appInfo.packageName);

            return convertView;
        }
    }
}
