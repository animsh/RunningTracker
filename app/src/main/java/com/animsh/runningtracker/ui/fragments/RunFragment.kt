package com.animsh.runningtracker.ui.fragments

import android.Manifest
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.animsh.runningtracker.R
import com.animsh.runningtracker.adapters.RunAdapter
import com.animsh.runningtracker.other.Constants.REQUEST_CODE_LOCATION_PERMISSION
import com.animsh.runningtracker.other.SortsType
import com.animsh.runningtracker.other.TrackingUtility
import com.animsh.runningtracker.ui.viewmodels.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.fragment_run.*
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions

@AndroidEntryPoint
class RunFragment : Fragment(R.layout.fragment_run), EasyPermissions.PermissionCallbacks {

    private val viewModel: MainViewModel by viewModels()

    private lateinit var runAdapter: RunAdapter

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        requestPermission()
        setupRecyclerView()

        when(viewModel.sortType){
            SortsType.DATE -> spFilter.setSelection(0)
            SortsType.RUNNING_TIME -> spFilter.setSelection(1)
            SortsType.DISTANCE -> spFilter.setSelection(2)
            SortsType.AVG_SPEED -> spFilter.setSelection(3)
            SortsType.CALORIES_BURNED -> spFilter.setSelection(4)
        }

        spFilter.onItemSelectedListener = object : AdapterView.OnItemSelectedListener{
            override fun onItemSelected(adapterView: AdapterView<*>?, view: View?, pos: Int, id: Long) {
                when(pos){
                    0 -> viewModel.sortRuns(SortsType.DATE)
                    1 -> viewModel.sortRuns(SortsType.RUNNING_TIME)
                    2 -> viewModel.sortRuns(SortsType.DISTANCE)
                    3 -> viewModel.sortRuns(SortsType.AVG_SPEED)
                    4 -> viewModel.sortRuns(SortsType.CALORIES_BURNED)
                }
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
            }
        }

        viewModel.runs.observe(viewLifecycleOwner, Observer {
            runAdapter.submitList(it)
        })

        fab.setOnClickListener {
            findNavController().navigate(R.id.action_runFragment_to_trackingFragment2)
        }
    }

    private fun setupRecyclerView() = rvRuns.apply {
        runAdapter = RunAdapter()
        adapter = runAdapter
        layoutManager = LinearLayoutManager(requireContext())
    }

    private fun requestPermission() {
        if (TrackingUtility.hasLocationPermissions(requireContext())) {
            return
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use the app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
        } else {
            EasyPermissions.requestPermissions(
                this,
                "You need to accept location permission to use the app.",
                REQUEST_CODE_LOCATION_PERMISSION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_BACKGROUND_LOCATION
            )
        }
    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(this,perms)){
            AppSettingsDialog.Builder(this).build().show()
        } else {
            requestPermission()
        }
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode,permissions,grantResults,this)
    }
}