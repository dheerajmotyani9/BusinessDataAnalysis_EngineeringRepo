package mapitgis.jalnigam.rfi.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RadioButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.SpinnerItem;

public class QAQCReviewAdapter extends RecyclerView.Adapter<QAQCReviewAdapter.QAQCReviewHolder> {

    private Context context;
    private List<SpinnerItem> qaqcReviewList;
    private QAQCReviewListener listener;

    int selectedPosition = -1;
    public QAQCReviewAdapter(Context context, List<SpinnerItem> qaqcReviewList, QAQCReviewListener listener) {
        this.context = context;
        this.qaqcReviewList = qaqcReviewList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public QAQCReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_qa_qc_review_list,parent,false);
        return new QAQCReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull QAQCReviewHolder holder, int position) {
        SpinnerItem review = qaqcReviewList.get(position);

        holder.radioButton.setText(review.getValue());

        holder.radioButton.setOnClickListener(v -> {
           selectedPosition = holder.getAdapterPosition();
           notifyDataSetChanged();
           listener.onQAQCReviewClicked(review);
        });

        if(selectedPosition == holder.getAdapterPosition()){
            holder.radioButton.setChecked(true);
        }else {
            holder.radioButton.setChecked(false);

        }
    }

    @Override
    public int getItemCount() {
        return qaqcReviewList.size();
    }

    public static class QAQCReviewHolder extends RecyclerView.ViewHolder{

        private final RadioButton radioButton;
        public QAQCReviewHolder(@NonNull View itemView) {
            super(itemView);
            radioButton = itemView.findViewById(R.id.layout_qa_qc_review_radio_btn);
        }
    }

    public interface QAQCReviewListener{
        void onQAQCReviewClicked(SpinnerItem qaqcReview);
    }
}
