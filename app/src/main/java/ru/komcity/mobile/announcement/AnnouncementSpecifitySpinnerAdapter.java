package ru.komcity.mobile.announcement;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.List;
import ru.komcity.mobile.R;

public class AnnouncementSpecifitySpinnerAdapter extends ArrayAdapter<AnnouncementSubCategoryItemModel> {
    private Context context;
    private LayoutInflater inflater = null;
    private List<AnnouncementSubCategoryItemModel> spinnerObjectList = new ArrayList<>();

    public AnnouncementSpecifitySpinnerAdapter(@NonNull Context context, int resource, List<AnnouncementSubCategoryItemModel> objects) {
        super(context, resource, objects);
        this.context = context;

        spinnerObjectList = objects;
        inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public View getDropDownView(int position, @Nullable final View convertView, @NonNull final ViewGroup parent) {
        View view = View.inflate(getContext(), R.layout.announcement_item_spinner_dropdown, null);
        AnnouncementSubCategoryItemModel item = (AnnouncementSubCategoryItemModel)getItem(position);

        TextView label = (TextView) view.findViewById(R.id.lbl_spinner_item_text);
        label.setText(item.Name);

        return view;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = inflater.inflate(R.layout.announcement_item_spinner, parent, false);
        AnnouncementSubCategoryItemModel item = (AnnouncementSubCategoryItemModel)spinnerObjectList.get(position);

        TextView label = (TextView) view.findViewById(R.id.lbl_spinner_item_text);
        label.setText(item.Name);

        return view;
    }
}