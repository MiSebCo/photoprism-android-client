package ua.com.radiokot.photoprism.features.gallery.search.people.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.AttributeSet
import android.view.Menu
import android.view.View
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.SearchView
import androidx.core.content.ContextCompat
import androidx.core.view.doOnPreDraw
import androidx.core.widget.ImageViewCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.color.MaterialColors
import com.google.android.material.snackbar.Snackbar
import com.mikepenz.fastadapter.FastAdapter
import com.mikepenz.fastadapter.adapters.ItemAdapter
import me.zhanghai.android.fastscroll.FastScrollerBuilder
import org.koin.androidx.viewmodel.ext.android.viewModel
import ua.com.radiokot.photoprism.R
import ua.com.radiokot.photoprism.base.view.BaseActivity
import ua.com.radiokot.photoprism.databinding.ActivityPeopleOverviewBinding
import ua.com.radiokot.photoprism.extension.autoDispose
import ua.com.radiokot.photoprism.extension.bindTextTwoWay
import ua.com.radiokot.photoprism.extension.kLogger
import ua.com.radiokot.photoprism.features.gallery.search.people.view.model.PeopleOverviewViewModel
import ua.com.radiokot.photoprism.features.gallery.search.people.view.model.PersonListItem
import ua.com.radiokot.photoprism.view.ErrorView

class PeopleOverviewActivity : BaseActivity() {
    private val log = kLogger("PeopleOverviewActivity")

    private lateinit var view: ActivityPeopleOverviewBinding
    private val viewModel: PeopleOverviewViewModel by viewModel()
    private val adapter = ItemAdapter<PersonListItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        view = ActivityPeopleOverviewBinding.inflate(layoutInflater)
        setContentView(view.root)

        setSupportActionBar(view.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        viewModel.init(
            currentlySelectedPersonIds =
            requireNotNull(intent.getStringArrayExtra(SELECTED_PERSON_IDS_EXTRA)?.toSet()) {
                "No $SELECTED_PERSON_IDS_EXTRA specified"
            }
        )

        // Init the list once it is laid out.
        view.peopleRecyclerView.doOnPreDraw {
            initList()
        }
        initErrorView()
        initSwipeRefresh()
        initButtons()

        subscribeToData()
        subscribeToEvents()

        // Allow the view model to intercept back press.
        onBackPressedDispatcher.addCallback(viewModel.backPressedCallback)
    }

    private fun initList() {
        val peopleAdapter = FastAdapter.with(adapter).apply {
            stateRestorationPolicy = RecyclerView.Adapter.StateRestorationPolicy.PREVENT_WHEN_EMPTY

            onClickListener = { _, _, item: PersonListItem, _ ->
                viewModel.onPersonItemClicked(item)
                true
            }
        }

        with(view.peopleRecyclerView) {
            // Safe dimensions of the list keeping from division by 0.
            // The fallback size is not supposed to be taken,
            // as it means initializing of a not laid out list.
            val listWidth = measuredWidth
                .takeIf { it > 0 }
                ?: FALLBACK_LIST_SIZE
                    .also {
                        log.warn { "initList(): used_fallback_width" }
                    }

            val minItemWidthPx =
                resources.getDimensionPixelSize(R.dimen.list_item_person_overview_min_width)
            val spanCount = (listWidth / minItemWidthPx).coerceAtLeast(1)

            log.debug {
                "initList(): calculated_grid:" +
                        "\nspanCount=$spanCount," +
                        "\nrowWidth=$listWidth," +
                        "\nminItemWidthPx=$minItemWidthPx"
            }

            adapter = peopleAdapter

            // Add the row spacing and make the items fill the column width
            // by overriding the layout manager layout params factory.
            layoutManager = object : GridLayoutManager(context, spanCount) {
                val rowSpacing: Int =
                    resources.getDimensionPixelSize(R.dimen.list_item_person_margin_end)

                override fun generateLayoutParams(
                    c: Context,
                    attrs: AttributeSet
                ): RecyclerView.LayoutParams {
                    return super.generateLayoutParams(c, attrs).apply {
                        width = RecyclerView.LayoutParams.MATCH_PARENT
                        bottomMargin = rowSpacing
                    }
                }
            }

            FastScrollerBuilder(this)
                .useMd2Style()
                .setTrackDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.fast_scroll_track
                    )!!
                )
                .setThumbDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.fast_scroll_thumb
                    )!!
                )
                .build()
        }
    }

    private fun initErrorView() {
        view.errorView.replaces(view.peopleRecyclerView)
    }

    private fun initSwipeRefresh() {
        view.swipeRefreshLayout.setColorSchemeColors(
            MaterialColors.getColor(
                view.swipeRefreshLayout,
                com.google.android.material.R.attr.colorPrimary,
            )
        )
        view.swipeRefreshLayout.setOnRefreshListener(viewModel::onSwipeRefreshPulled)
    }

    private fun initButtons() {
        view.doneSelectingFab.setOnClickListener {
            viewModel.onDoneClicked()
        }
    }

    private fun subscribeToData() {
        viewModel.itemsList.observe(this, adapter::setNewList)

        viewModel.isLoading.observe(this, view.swipeRefreshLayout::setRefreshing)

        viewModel.mainError.observe(this) { mainError ->
            when (mainError) {
                PeopleOverviewViewModel.Error.LoadingFailed ->
                    view.errorView.showError(
                        ErrorView.Error.General(
                            context = view.errorView.context,
                            messageRes = R.string.failed_to_load_people,
                            retryButtonTextRes = R.string.try_again,
                            retryButtonClickListener = viewModel::onRetryClicked
                        )
                    )

                PeopleOverviewViewModel.Error.NothingFound ->
                    view.errorView.showError(
                        ErrorView.Error.EmptyView(
                            context = view.errorView.context,
                            messageRes = R.string.no_people_found,
                        )
                    )

                null ->
                    view.errorView.hide()
            }
        }

        viewModel.isDoneButtonVisible.observe(this) { isDoneButtonVisible ->
            if (isDoneButtonVisible) {
                view.doneSelectingFab.show()
            } else {
                view.doneSelectingFab.hide()
            }
        }
    }

    private fun subscribeToEvents() = viewModel.events.subscribe { event ->
        log.debug {
            "subscribeToEvents(): received_new_event:" +
                    "\nevent=$event"
        }

        when (event) {
            PeopleOverviewViewModel.Event.ShowFloatingLoadingFailedError ->
                showFloatingLoadingFailedError()

            is PeopleOverviewViewModel.Event.FinishWithResult ->
                finishWithResult(event.selectedPersonIds)

            is PeopleOverviewViewModel.Event.Finish ->
                finish()
        }

        log.debug {
            "subscribeToEvents(): handled_new_event:" +
                    "\nevent=$event"
        }
    }.autoDispose(this)

    private fun showFloatingLoadingFailedError() {
        Snackbar.make(
            view.swipeRefreshLayout,
            getString(R.string.failed_to_load_people),
            Snackbar.LENGTH_SHORT
        )
            .setAction(R.string.try_again) { viewModel.onRetryClicked() }
            .show()
    }

    private fun finishWithResult(selectedPersonIds: Set<String>) {
        log.debug {
            "finishWithResult(): finishing:" +
                    "\nselectedPeopleCount=${selectedPersonIds.size}"
        }

        setResult(
            Activity.RESULT_OK,
            Intent().putExtras(
                createResult(selectedPersonIds)
            )
        )
        finish()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.albums, menu)

        // TODO Create a component for search view
        // Set up the search.
        with(menu?.findItem(R.id.search_view)?.actionView as SearchView) {
            queryHint = getString(R.string.enter_the_query)

            // Apply the correct color for the search close button.
            with(findViewById<ImageView>(androidx.appcompat.R.id.search_close_btn)) {
                ImageViewCompat.setImageTintList(
                    this, ColorStateList.valueOf(
                        MaterialColors.getColor(
                            view.toolbar, com.google.android.material.R.attr.colorOnSurfaceVariant
                        )
                    )
                )
            }

            // Remove the underline.
            with(findViewById<View>(androidx.appcompat.R.id.search_plate)) {
                background = null
            }

            // Directly bind the input.
            findViewById<EditText>(androidx.appcompat.R.id.search_src_text)
                .bindTextTwoWay(viewModel.searchInput, this@PeopleOverviewActivity)

            // Bind the collapse/expand state.
            viewModel.isSearchExpanded.observe(this@PeopleOverviewActivity) { isExpanded ->
                // This also triggers the following listeners.
                isIconified = !isExpanded
            }
            setOnSearchClickListener {
                viewModel.onSearchIconClicked()
            }
            setOnCloseListener {
                viewModel.onSearchCloseClicked()
                false
            }
        }

        return super.onCreateOptionsMenu(menu)
    }

    companion object {
        private const val FALLBACK_LIST_SIZE = 100
        private const val SELECTED_PERSON_IDS_EXTRA = "selected_person_ids"

        fun getBundle(selectedPersonIds: Set<String>) = Bundle().apply {
            putStringArray(SELECTED_PERSON_IDS_EXTRA, selectedPersonIds.toTypedArray())
        }

        private fun createResult(selectedPersonIds: Set<String>) = Bundle().apply {
            putStringArray(SELECTED_PERSON_IDS_EXTRA, selectedPersonIds.toTypedArray())
        }

        fun getSelectedPersonIds(bundle: Bundle): Set<String> =
            requireNotNull(bundle.getStringArray(SELECTED_PERSON_IDS_EXTRA)?.toSet()) {
                "No $SELECTED_PERSON_IDS_EXTRA specified"
            }
    }
}