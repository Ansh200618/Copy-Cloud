package com.copycloud.app.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.copycloud.app.R;
import com.copycloud.app.models.FileItem;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class FileAdapter extends RecyclerView.Adapter<FileAdapter.FileViewHolder> {
    
    private final List<FileItem> files;
    private final OnFileClickListener listener;
    
    public interface OnFileClickListener {
        void onDownloadClick(FileItem file);
    }
    
    public FileAdapter(List<FileItem> files, OnFileClickListener listener) {
        this.files = files;
        this.listener = listener;
    }
    
    @NonNull
    @Override
    public FileViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_file, parent, false);
        return new FileViewHolder(view);
    }
    
    @Override
    public void onBindViewHolder(@NonNull FileViewHolder holder, int position) {
        FileItem file = files.get(position);
        holder.bind(file, listener);
    }
    
    @Override
    public int getItemCount() {
        return files.size();
    }
    
    static class FileViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvFileName;
        private final TextView tvFileSize;
        private final MaterialButton btnDownload;
        
        public FileViewHolder(@NonNull View itemView) {
            super(itemView);
            tvFileName = itemView.findViewById(R.id.tvFileName);
            tvFileSize = itemView.findViewById(R.id.tvFileSize);
            btnDownload = itemView.findViewById(R.id.btnDownload);
        }
        
        public void bind(FileItem file, OnFileClickListener listener) {
            tvFileName.setText(file.getName());
            tvFileSize.setText(file.getFormattedSize());
            btnDownload.setOnClickListener(v -> listener.onDownloadClick(file));
        }
    }
}
