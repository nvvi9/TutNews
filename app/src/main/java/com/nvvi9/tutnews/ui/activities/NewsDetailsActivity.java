package com.nvvi9.tutnews.ui.activities;

import android.os.Bundle;
import android.text.Editable;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProvider;

import com.nvvi9.tutnews.R;
import com.nvvi9.tutnews.data.LoadState;
import com.nvvi9.tutnews.databinding.ActivityNewsDetailsBinding;
import com.nvvi9.tutnews.ui.viewmodels.NewsDetailsViewModel;
import com.nvvi9.tutnews.utils.ViewUtils;

import javax.inject.Inject;

import dagger.android.AndroidInjection;

public class NewsDetailsActivity extends AppCompatActivity {

    private ActivityNewsDetailsBinding binding;

    @Inject
    public ViewModelProvider.Factory viewModelFactory;

    private NewsDetailsViewModel newsDetailsViewModel;

    private boolean isInEditMode = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        AndroidInjection.inject(this);
        super.onCreate(savedInstanceState);

        binding = DataBindingUtil.setContentView(this, R.layout.activity_news_details);

        newsDetailsViewModel = new ViewModelProvider(this, viewModelFactory).get(NewsDetailsViewModel.class);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            newsDetailsViewModel.setNewsItem(bundle.getInt("id"));
        }

        setupViewModelObservers();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (isInEditMode && keyCode == KeyEvent.KEYCODE_BACK) {
            editDescriptionDone(binding.closeButton);
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void editDescription(View v) {
        binding.doneButton.setVisibility(View.VISIBLE);
        binding.closeButton.setVisibility(View.VISIBLE);
        binding.editButton.setVisibility(View.GONE);
        binding.backButton.setVisibility(View.GONE);
        binding.viewSwitcher.showNext();
        isInEditMode = true;
        binding.descriptionEditText.requestFocus();
        ViewUtils.showKeyboard(binding.descriptionEditText);
    }

    public void editDescriptionDone(View v) {
        Editable updatedDescription = binding.descriptionEditText.getText();
        CharSequence oldDescription = binding.descriptionTextView.getText();

        if (binding.doneButton == v && updatedDescription != null) {
            newsDetailsViewModel.updateNewsItemDescription(updatedDescription.toString());
        } else if (binding.closeButton == v && oldDescription != null) {
            binding.descriptionEditText.setText(oldDescription);
        }

        binding.editButton.setVisibility(View.VISIBLE);
        binding.backButton.setVisibility(View.VISIBLE);
        binding.closeButton.setVisibility(View.GONE);
        binding.doneButton.setVisibility(View.GONE);
        binding.viewSwitcher.showPrevious();
        isInEditMode = false;
        ViewUtils.hideKeyboard(binding.descriptionEditText);
    }

    public void finishActivity(View v) {
        finish();
    }

    private void setupViewModelObservers() {
        newsDetailsViewModel.getNewsItem().observe(this, newsItem -> {
            if (newsItem != null) {
                binding.setNewsItem(newsItem);
            }
        });

        newsDetailsViewModel.getLoadState().observe(this, loadState -> {
            if (loadState instanceof LoadState.Error) {
                Toast.makeText(this, ((LoadState.Error) loadState).getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
