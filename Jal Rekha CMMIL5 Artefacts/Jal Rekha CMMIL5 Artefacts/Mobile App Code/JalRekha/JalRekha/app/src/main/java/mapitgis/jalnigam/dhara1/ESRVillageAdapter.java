package mapitgis.jalnigam.dhara1;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

import mapitgis.jalnigam.BuildConfig;
import mapitgis.jalnigam.R;
import mapitgis.jalnigam.core.MyTextWatcher;
import mapitgis.jalnigam.core.SpinnerItem;
import mapitgis.jalnigam.core.SqLite;
import mapitgis.jalnigam.core.Utility;
import mapitgis.jalnigam.databinding.AdapterEsrVillageBinding;


public abstract class ESRVillageAdapter extends RecyclerView.Adapter<ESRVillageAdapter.ViewHolder> {
    private final List<ESRVillage> esrVillages;
    private final Activity activity;

    public ESRVillageAdapter(@NonNull Activity activity) {
        this.esrVillages = new ArrayList<>();
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(AdapterEsrVillageBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
    }

    public void clear() {
        int len = this.esrVillages.size();
        this.esrVillages.clear();
        this.notifyItemRangeRemoved(0, len);
    }

    public void add(ESRVillage dharaComponent) {
        int pos = getItemCount();
        this.esrVillages.add(dharaComponent);
        this.notifyItemInserted(pos);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ESRVillage esrVillage = esrVillages.get(position);
        if (esrVillage.meterStatusAdapter == null) {
            esrVillage.meterStatusAdapter = new MeterStatusAdapter(activity, position, SqLite.instance(activity).GET_DHARA_METER_STATUS()) {
                @Override
                void onChange(SpinnerItem spinnerItem) {
                    onChangeNotify(spinnerItem, extra);
                }
            };
            esrVillage.meterStatusAdapter.setSelectedItem(esrVillage.getMeterStatusId());
        }
        if (esrVillage.setUpdatedReading) {
            esrVillage.setUpdatedReading = false;
            esrVillage.meterStatusAdapter.setSelectedItem(esrVillage.getMeterStatusId());
        }


        holder.binding.recyclerViewStatus.setAdapter(esrVillage.meterStatusAdapter);

        if (esrVillage.isFilled()) {
            esrVillage.meterStatusAdapter.enable = false;
            holder.binding.recyclerViewStatus.setEnabled(false);
            holder.binding.editTextStartReading.setEnabled(false);
            holder.binding.editTextTotalWS.setEnabled(false);
            holder.binding.editTextEndReading.setEnabled(false);
            holder.binding.editTextEndRemark.setEnabled(false);

//        holder.binding.editTextStartReading.setText(esrVillage.getR1Reading());
            holder.binding.editTextStartReading.setText(esrVillage.getRSReading());

            holder.binding.editTextEndReading.setText(esrVillage.getR2Reading());
            holder.binding.editTextEndRemark.setText(esrVillage.getRemarkWithNA());


            Glide.with(activity).load(BuildConfig.JAL_NIGAM_IMAGE + esrVillage.getPhoto()).into(holder.binding.imageViewPhoto);
        } else {
            esrVillage.meterStatusAdapter.enable = true;
            holder.binding.recyclerViewStatus.setEnabled(true);
            switch (esrVillage.meterStatusAdapter.getSelectedItem().getKey()) {
                case 2:
                    holder.binding.editTextStartReading.setEnabled(true);
                    holder.binding.editTextEndReading.setEnabled(true);
                    holder.binding.editTextTotalWS.setEnabled(false);
                    break;
                case 3:
                    holder.binding.editTextStartReading.setEnabled(false);
                    holder.binding.editTextEndReading.setEnabled(false);
                    holder.binding.editTextTotalWS.setEnabled(true);
                    break;
                case 4:
                    holder.binding.editTextStartReading.setEnabled(false);
                    holder.binding.editTextEndReading.setEnabled(false);
                    holder.binding.editTextTotalWS.setEnabled(false);
                    break;
                default:
                    holder.binding.editTextStartReading.setEnabled(false);
                    holder.binding.editTextEndReading.setEnabled(true);
                    holder.binding.editTextTotalWS.setEnabled(false);
                    break;
            }

//        holder.binding.editTextStartReading.setText(esrVillage.getR1Reading());
            holder.binding.editTextStartReading.setText(esrVillage.getRSReading());

//            holder.binding.editTextEndReading.setEnabled(true);
            holder.binding.editTextEndRemark.setEnabled(true);
            if (esrVillage.getRE() > 0) {
                holder.binding.editTextEndReading.setText(esrVillage.getREReading());
            } else {
                holder.binding.editTextEndReading.setText("");
            }
            holder.binding.editTextEndRemark.setText(esrVillage.getRemark());
            if (esrVillage.getPhoto().isEmpty()) {
                holder.binding.imageViewPhoto.setImageResource(R.drawable.add_image);
//                holder.binding.imageViewPhoto.setTag(null);
            } else {
                holder.binding.imageViewPhoto.setImageBitmap(Utility.bitmap(esrVillage.getPhoto()));
//                holder.binding.imageViewPhoto.setTag(esrVillage.getPhoto());
            }
        }
        if (esrVillage.getR1Date().isEmpty()) {
            holder.binding.textViewR1Date.setVisibility(View.GONE);
            holder.binding.textViewR1Date.setText("");
        } else {
            holder.binding.textViewR1Date.setVisibility(View.VISIBLE);
            holder.binding.textViewR1Date.setText(esrVillage.getR1DateDetail());
        }
        holder.binding.editTextTotalWS.setText(esrVillage.getWSReading());
        holder.binding.textViewName.setText(String.format("%s [%s]", esrVillage.getName(), esrVillage.getLgd()));
        if (esrVillage.getHCd() == null || esrVillage.getHCd().isEmpty()) {
            holder.binding.textViewHabitation.setVisibility(View.GONE);
        } else {
            holder.binding.textViewHabitation.setVisibility(View.VISIBLE);
            holder.binding.textViewHabitation.setText(esrVillage.getHCd());
        }
        holder.binding.getRoot().setTag(holder.binding);
    }

    private void onChangeNotify(@NonNull SpinnerItem spinnerItem, int position) {
        esrVillages.get(position).setRS(esrVillages.get(position).getR1());
        esrVillages.get(position).setRE(spinnerItem.getKey() == 1 ? 0 : esrVillages.get(position).getR1());
        notifyItemChanged(position);
    }

    @Override
    public int getItemCount() {
        return esrVillages.size();
    }

    @SuppressLint("NotifyDataSetChanged")
    public void addAll(List<ESRVillage> dharaComponentList) {
        this.esrVillages.addAll(dharaComponentList);
        this.notifyDataSetChanged();
    }

    @NonNull
    public ESRVillage getItem(int i) {
        return esrVillages.get(i);
    }

    public void remove(int pos) {
        this.esrVillages.remove(pos);
        this.notifyItemRemoved(pos);
    }

//    public void updateUploaded(int pos) {
//        this.dharaComponentList.get(pos).setUploaded(true);
//        this.notifyItemChanged(pos);
//    }

    public boolean isEmpty() {
        return this.esrVillages.isEmpty();
    }

    public void setImageAndLatLong(int pos, LatLng latLng, Bitmap bitmap) {
        this.esrVillages.get(pos).setPhoto(Utility.base64(bitmap));
        this.esrVillages.get(pos).setLatLng(latLng);
        this.notifyItemChanged(pos);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private final AdapterEsrVillageBinding binding;

        public ViewHolder(@NonNull AdapterEsrVillageBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            binding.editTextStartReading.addTextChangedListener(new MyTextWatcher(binding.getRoot()) {
                @Override
                protected void afterTextChanged(@NonNull View view, @NonNull String text) {
                    try {
                        int position = getAdapterPosition();
                        if (esrVillages.get(position).meterStatusAdapter.getSelectedItem().getKey() == 2) {
                            AdapterEsrVillageBinding binding1 = (AdapterEsrVillageBinding) view.getTag();
                            esrVillages.get(position).setRS(Double.parseDouble(text));
                            binding1.editTextTotalWS.setText(esrVillages.get(position).updateTWSAuto());
                        }
//                        notifyItemChanged(position);
                    } catch (Exception ignore) {

                    }
                }
            });
            binding.editTextEndReading.addTextChangedListener(new MyTextWatcher(binding.getRoot()) {
                @Override
                protected void afterTextChanged(@NonNull View view, @NonNull String text) {
                    try {
                        int position = getAdapterPosition();
                        if (esrVillages.get(position).meterStatusAdapter.getSelectedItem().getKey() == 1 || esrVillages.get(position).meterStatusAdapter.getSelectedItem().getKey() == 2) {
                            AdapterEsrVillageBinding binding1 = (AdapterEsrVillageBinding) view.getTag();
                            esrVillages.get(position).setRE(Double.parseDouble(text));
                            binding1.editTextTotalWS.setText(esrVillages.get(position).updateTWSAuto());
                        }
//                        notifyItemChanged(position);
                    } catch (Exception ignore) {

                    }
                }
            });
            binding.editTextTotalWS.addTextChangedListener(new MyTextWatcher(binding.getRoot()) {
                @Override
                protected void afterTextChanged(@NonNull View view, @NonNull String text) {
                    try {
                        int position = getAdapterPosition();
                        if (esrVillages.get(position).meterStatusAdapter.getSelectedItem().getKey() == 3) {
//                            AdapterEsrVillageBinding binding1 = (AdapterEsrVillageBinding) view.getTag();
                            esrVillages.get(position).setWs(Double.parseDouble(text));
//                            binding1.editTextEndReading.setText(esrVillages.get(position).getREReading());
                        }
//                        notifyItemChanged(position);
                    } catch (Exception ignore) {

                    }
                }
            });
            binding.editTextEndRemark.addTextChangedListener(new MyTextWatcher(binding.getRoot()) {
                @Override
                protected void afterTextChanged(@NonNull View view, @NonNull String text) {
                    try {
                        int position = getAdapterPosition();
                        esrVillages.get(position).setRemark(text);
                    } catch (Exception ignore) {

                    }
                }
            });

            binding.imageViewPhoto.setOnClickListener(v -> {
                int pos = getAdapterPosition();
                onSelectPhoto(pos, esrVillages.get(pos));
            });
//            binding.buttonDetail.setOnClickListener(v -> {
//                int pos = getAdapterPosition();
//                onView(pos, dharaComponentList.get(pos));
//            });
        }
    }

    protected abstract void onSelectPhoto(int pos, @NonNull ESRVillage esrVillage);
//
//    protected abstract void onDiscard(int pos,@NonNull ESRVillage dharaComponent);

    protected abstract void onView(int pos, @NonNull ESRVillage dharaComponent);
}
