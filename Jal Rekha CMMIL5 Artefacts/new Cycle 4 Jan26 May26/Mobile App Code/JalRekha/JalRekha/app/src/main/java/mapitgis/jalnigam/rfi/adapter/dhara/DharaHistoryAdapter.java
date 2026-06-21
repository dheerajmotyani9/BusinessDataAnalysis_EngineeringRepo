/**
 * Created on : 10-09-2024
 */
package mapitgis.jalnigam.rfi.adapter.dhara;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.rfi.model.dhara.DharaHistory;

public class DharaHistoryAdapter extends RecyclerView.Adapter<DharaHistoryAdapter.DharaHistoryViewHolder> {

    private Context context;
    private List<DharaHistory> dharaHistoryList;
    private DharaHistoryListener listener;

    public DharaHistoryAdapter(Context context, List<DharaHistory> dharaHistoryList, DharaHistoryListener listener) {
        this.context = context;
        this.dharaHistoryList = dharaHistoryList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public DharaHistoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_dhara_history_list, parent, false);
        return new DharaHistoryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DharaHistoryViewHolder holder, int position) {
        DharaHistory history = dharaHistoryList.get(position);
        holder.btnViewDetail.setOnClickListener(v -> listener.onViewDetailClicked(position, history));
    }

    @Override
    public int getItemCount() {
        return dharaHistoryList.size();
    }

    public static class DharaHistoryViewHolder extends RecyclerView.ViewHolder {

        private Button btnViewDetail;

        public DharaHistoryViewHolder(@NonNull View itemView) {
            super(itemView);
            btnViewDetail = itemView.findViewById(R.id.layout_dhara_history_btn_detail);
        }
    }

    public interface DharaHistoryListener {
        void onViewDetailClicked(int position, DharaHistory history);
    }
}
