package com.copycloud.app.adapters;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.copycloud.app.R;
import com.copycloud.app.models.HistoryItem;
import com.copycloud.app.utils.DeviceManager;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.ViewHolder> {
    
    private final Context context;
    private List<HistoryItem> items = new ArrayList<>();
    private final OnItemClickListener clickListener;
    private final OnItemDeleteListener deleteListener;
    
    public interface OnItemClickListener {
        void onItemClick(HistoryItem item);
    }
    
    public interface OnItemDeleteListener {
        void onItemDelete(HistoryItem item);
    }
    
    public HistoryAdapter(Context context, OnItemClickListener clickListener, OnItemDeleteListener deleteListener) {
        this.context = context;
        this.clickListener = clickListener;
        this.deleteListener = deleteListener;
    }
    
    public void setItems(List<HistoryItem> items) {
        this.items = items;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_history, parent, false);
        return new ViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        HistoryItem item = items.get(position);
        holder.bind(item);
    }
    
    @Override
    public int getItemCount() {
        return items.size();
    }
    
    class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvCode;
        private final TextView tvTimestamp;
        private final TextView tvType;
        private final TextView tvContentPreview;
        private final TextView tvDeviceCode;
        private final MaterialButton btnDelete;
        
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvCode = itemView.findViewById(R.id.tvCode);
            tvTimestamp = itemView.findViewById(R.id.tvTimestamp);
            tvType = itemView.findViewById(R.id.tvType);
            tvContentPreview = itemView.findViewById(R.id.tvContentPreview);
            tvDeviceCode = itemView.findViewById(R.id.tvDeviceCode);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
        
        public void bind(HistoryItem item) {
            tvCode.setText(item.getCode());
            tvTimestamp.setText(item.getTimeAgo());
            tvType.setText(item.getType().toUpperCase());
            
            // Show content preview
            String preview = item.getContent();
            if (preview != null) {
                if (preview.length() > 100) {
                    preview = preview.substring(0, 100) + "...";
                }
                tvContentPreview.setText(preview);
            } else {
                tvContentPreview.setText("No preview available");
            }
            
            // Show device code if targeted
            if (item.getDeviceCode() != null && !item.getDeviceCode().isEmpty()) {
                tvDeviceCode.setVisibility(View.VISIBLE);
                tvDeviceCode.setText("📱 Device: " + DeviceManager.formatDeviceCode(item.getDeviceCode()));
            } else {
                tvDeviceCode.setVisibility(View.GONE);
            }
            
            // Click handlers
            itemView.setOnClickListener(v -> {
                if (clickListener != null) {
                    clickListener.onItemClick(item);
                }
            });
            
            // Long click to copy code
            itemView.setOnLongClickListener(v -> {
                ClipboardManager clipboard = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Code", item.getCode());
                clipboard.setPrimaryClip(clip);
                Toast.makeText(context, "Code copied: " + item.getCode(), Toast.LENGTH_SHORT).show();
                return true;
            });
            
            btnDelete.setOnClickListener(v -> {
                if (deleteListener != null) {
                    deleteListener.onItemDelete(item);
                }
            });
        }
    }
}
