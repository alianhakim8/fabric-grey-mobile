package id.alian.fabric_mobile_mvvm.ui.main.view

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import id.alian.fabric_mobile_mvvm.R
import id.alian.fabric_mobile_mvvm.databinding.ActivityDashboardBinding
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.FabricFragment
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.HomeFragment
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.MachineFragment
import id.alian.fabric_mobile_mvvm.ui.main.view.fragment.ReportFragment

class DashboardActivity : AppCompatActivity() {

    private lateinit var b: ActivityDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        b = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(b.root)

        val homeFragment = HomeFragment()
        val fabricFragment = FabricFragment()
        val machineFragment = MachineFragment()
        val reportFragment = ReportFragment()

        supportFragmentManager.beginTransaction().apply {
            replace(R.id.fragment, homeFragment)
            commit()
        }

        b.bottomNavigation.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.home -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, homeFragment)
                        this.setCustomAnimations(R.animator.fade_in, R.animator.fade_out)
                        commit()
                    }
                    true
                }

                R.id.fabric -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, fabricFragment)
                        commit()
                    }
                    true
                }

                R.id.machine -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, machineFragment)
                        commit()
                    }
                    true
                }

                R.id.report -> {
                    supportFragmentManager.beginTransaction().apply {
                        replace(R.id.fragment, reportFragment)
                        commit()
                    }
                    true
                }
                else -> false
            }
        }
    }
}