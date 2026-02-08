package com.copycloud.app.fragments;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.copycloud.app.R;
import com.copycloud.app.adapters.HistoryAdapter;
import com.copycloud.app.database.HistoryDatabase;
import com.copycloud.app.models.HistoryItem;
import com.copycloud.app.utils.ThemeManager;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class HistoryFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private HistoryAdapter adapter;
    private HistoryDatabase historyDb;
    private LinearLayout emptyState;
    private MaterialButton btnClearHistory;
    private SwipeRefreshLayout swipeRefresh;
    
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_history, container, false);
    }
    
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        
        historyDb = new HistoryDatabase(requireContext());
        
        initViews(view);
        setupRecyclerView();
        setupSwipeRefresh();
        loadHistory();
    }
    
    private void initViews(View view) {
        recyclerView = view.findViewById(R.id.recyclerViewHistory);
        emptyState = view.findViewById(R.id.emptyState);
        btnClearHistory = view.findViewById(R.id.btnClearHistory);
        swipeRefresh = view.findViewById(R.id.swipeRefresh);
        
        btnClearHistory.setOnClickListener(v -> showClearHistoryDialog());
    }
    
    private void setupSwipeRefresh() {
        // Set color scheme for refresh indicator
        int primaryColor = ThemeManager.getPrimaryColor(requireContext());
        swipeRefresh.setColorSchemeColors(primaryColor);
        swipeRefresh.setProgressBackgroundColorSchemeColor(
            ThemeManager.getBackgroundColor(requireContext())
        );
        
        swipeRefresh.setOnRefreshListener(() -> {
            loadHistory();
            swipeRefresh.setRefreshing(false);
            Toast.makeText(requireContext(), "History refreshed", Toast.LENGTH_SHORT).show();
        });
    }
    
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        adapter = new HistoryAdapter(requireContext(), item -> {
            // Handle item click - navigate to retrieve tab with code
            // TODO: Implement navigation
        }, item -> {
            // Handle delete
            deleteItem(item);
        });
        recyclerView.setAdapter(adapter);
    }
    
    private void loadHistory() {
        List<HistoryItem> history = historyDb.getAllHistory();
        adapter.setItems(history);
        
        if (history.isEmpty()) {
            emptyState.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        } else {
            emptyState.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }
    }
    
    private void deleteItem(HistoryItem item) {
        new AlertDialog.Builder(requireContext())
                .setTitle("Delete Item")
                .setMessage("Are you sure you want to delete this item?")
                .setPositiveButton("Delete", (dialog, which) -> {
                    historyDb.deleteHistoryItem(item.getId());
                    loadHistory();
                    Toast.makeText(requireContext(), "Item deleted", Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    private void showClearHistoryDialog() {
        List<HistoryItem> history = historyDb.getAllHistory();
        if (history.isEmpty()) {
            Toast.makeText(requireContext(), "History is already empty", Toast.LENGTH_SHORT).show();
            return;
        }
        
        new AlertDialog.Builder(requireContext())
                .setTitle("Clear All History")
                .setMessage("Are you sure you want to delete all " + history.size() + " items?")
                .setPositiveButton("Clear All", (dialog, which) -> {
                    historyDb.clearAllHistory();
                    loadHistory();
                    Toast.makeText(requireContext(), R.string.history_cleared, Toast.LENGTH_SHORT).show();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }
    
    @Override
    public void onResume() {
        super.onResume();
        // Refresh history when returning to this tab
        loadHistory();
    }
}
