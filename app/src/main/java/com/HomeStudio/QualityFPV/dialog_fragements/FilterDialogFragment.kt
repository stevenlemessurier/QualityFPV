package com.HomeStudio.QualityFPV.dialog_fragements

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.DialogFragment
import androidx.lifecycle.ViewModelProvider
import com.HomeStudio.QualityFPV.data.FilterViewModel
import com.HomeStudio.QualityFPV.MainActivity
import com.HomeStudio.QualityFPV.R

// Dialog fragment that sets the price range of products to scrape on each website
class FilterDialogFragment: DialogFragment() {
    private lateinit var mFilterViewModel: FilterViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val root = inflater.inflate(R.layout.dialog_fragment_filter, container, false)
        mFilterViewModel = ViewModelProvider(activity as MainActivity).get(FilterViewModel::class.java)

        val cancel = root.findViewById<Button>(R.id.cancel_button)
        val apply = root.findViewById<Button>(R.id.apply_button)
        val min = root.findViewById<EditText>(R.id.min)
        val max = root.findViewById<EditText>(R.id.max)


        cancel.setOnClickListener{
            this.dismiss()
        }

        apply.setOnClickListener {
            if(min.text.toString().isEmpty() && max.text.toString().isEmpty()){
                mFilterViewModel.setMin(0.0)
                mFilterViewModel.setMax(10000.0)
            }

            else if(min.text.toString().isNotEmpty() && max.text.toString().isEmpty()){
                mFilterViewModel.setMin(min.text.toString().toDouble())
                mFilterViewModel.setMax(10000.0)
            }

            else if(min.text.toString().isEmpty() && max.text.toString().isNotEmpty()){
                mFilterViewModel.setMin(0.0)
                mFilterViewModel.setMax(max.text.toString().toDouble())
            }

            else{
                mFilterViewModel.setMin(min.text.toString().toDouble())
                mFilterViewModel.setMax(max.text.toString().toDouble())
            }

            mFilterViewModel.setChange(true)
            this.dismiss()
        }

        return root
    }
}