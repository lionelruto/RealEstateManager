
package com.openclassrooms.realestatemanager.views.fragments

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.Models.Estate
import com.openclassrooms.realestatemanager.Models.ListPhotos
import com.openclassrooms.realestatemanager.ViewModel.EstateFactory
import com.openclassrooms.realestatemanager.ViewModel.EstateViewModel
import com.openclassrooms.realestatemanager.adapter.EstateAdapter
import com.openclassrooms.realestatemanager.databinding.FragmentListContainerBinding
import com.openclassrooms.realestatemanager.hilt.module.EstatesRepository
import com.openclassrooms.realestatemanager.views.ListItemListener
import com.openclassrooms.realestatemanager.views.MainActivity
import dagger.hilt.android.AndroidEntryPoint
import java.io.Serializable
import javax.inject.Inject

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [ListContainerFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
@AndroidEntryPoint
class ListContainerFragment : Fragment(), ListItemListener {

    private lateinit var fragmentbiding: FragmentListContainerBinding
    private lateinit var rv: RecyclerView
    private lateinit var emptyview: LinearLayout
    private lateinit var myestate: ArrayList<Estate>


    //private val binding get() = _binding!!

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null
    private lateinit var estateAdapter : EstateAdapter
    private lateinit var viewModel: EstateViewModel
    //val model by viewModels<EstateViewModel>()
    private lateinit var viewModelFactory: EstateFactory
    @Inject
    lateinit var estatesRepository: EstatesRepository
    private  var estates: ArrayList<Estate> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        fragmentbiding = FragmentListContainerBinding.inflate(inflater, container, false)
        rv = fragmentbiding.firstActivityRecyclerView
        rv?.layoutManager = LinearLayoutManager(context)
        emptyview = fragmentbiding.emptyView
        //initialisation du ViewModel
        //val viewModel: EstateViewModel by ViewModel {EstateFactory(estatesRepository)}
        viewModelFactory = EstateFactory(estatesRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(EstateViewModel::class.java)
        /*val photosList: ArrayList<ListPhotos> = ArrayList()
        val interet: ArrayList<String> = ArrayList()
        interet.add("immobilier")
        photosList.add(ListPhotos("Uri", "la description de l'image"))
        estates.add(
            Estate(1,
                "type",
                      photosList,
                3000,
                50,
                3,
                "FLAT",
                "rue boue de lapeyrere",
                interet,
                0,
                "20-04-2020",
                "20-04-2020",
                "Lionel Mambingo")
        )

        //initialisation du ViewModel
        //val viewModel: EstateViewModel by ViewModel {EstateFactory(estatesRepository)}
        viewModelFactory = EstateFactory(estatesRepository)
        viewModel = ViewModelProvider(this,viewModelFactory).get(EstateViewModel::class.java)

        viewModel.insertEstate(estates.get(0))*/

        /*viewModel.getAllEstate().observe(viewLifecycleOwner, Observer<List<Estate>>{ estate ->
            Toast.makeText(context,"${estate.get(0)}", Toast.LENGTH_SHORT).show()

            //initialisation du recyclerView
            val rv = fragmentbiding.firstActivityRecyclerView
            rv?.layoutManager = LinearLayoutManager(context)
            estateAdapter= EstateAdapter(estate,this )
            if (rv != null) {
                rv.adapter= estateAdapter
            }



        })*/
        val bundle = requireActivity().intent.getSerializableExtra("estates")
        val allestates: LiveData<List<Estate>>? = viewModel.getAllEstate()

            //vérifier si la base de donnée locale contient les donnés
            if (allestates == null){
                //afficher la vue qui informe qu'il n'ya pas de données
                rv.visibility= View.GONE
                emptyview.visibility= View.VISIBLE

            }
            else {
                //verifier si le bundle renvoyé par l'activité est null
                if (bundle == null){
                    Toast.makeText(context,"argument is null", Toast.LENGTH_SHORT).show()
                    rv.visibility= View.VISIBLE
                    emptyview.visibility= View.GONE
                    getData(allestates)

                }//quand le bundle passé par intent contient un enregistrement
                else{
                    //retirer la vue en cas de liste vide et afficher le recyclerview

                    rv.visibility= View.VISIBLE
                    emptyview.visibility= View.GONE
                    getData(allestates)
                }

            }

        return fragmentbiding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment ListContainerFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ListContainerFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }

    fun getData(mydata: LiveData<List<Estate>>){

        mydata.observe(requireActivity(), Observer <List<Estate>> { myEstates ->
            Log.d("TAG", "nous sommes dans le observer")
            myestate= myEstates as ArrayList<Estate>
            estateAdapter= EstateAdapter(myestate,this )
            //initialisation du recyclerView
            estateAdapter= EstateAdapter(myestate,this )
            estateAdapter.notifyDataSetChanged()
            if (rv != null) {
                rv.adapter= estateAdapter
            }

        })

    }
    override fun onItemclik(index: Int) {
        val intent= Intent(context, MainActivity::class.java)
        intent.putExtra("oneEstate", myestate.get(index))
        startActivity(intent)
    }

    //used to resolve the deprecated getSerializable
    fun <T : Serializable?> getSerializable(intent: Intent, key: String, m_class: Class<T>): T {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU)
            intent.getSerializableExtra(key, m_class)!!
        else
            intent.getSerializableExtra(key) as T
    }
}