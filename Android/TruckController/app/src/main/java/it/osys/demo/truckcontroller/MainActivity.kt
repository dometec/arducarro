package it.osys.demo.truckcontroller

import android.bluetooth.BluetoothAdapter
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import java.io.OutputStream
import java.util.*

class MainActivity : AppCompatActivity() {

    val uuid = UUID.fromString("00001101-0000-1000-8000-00805F9B34FB")
    val btAddress = "07:12:04:11:80:52"
    val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()

    var outputStream : OutputStream? = null
    var leftValue = 0
    var rightValue = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        seekBarLeft.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
            override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                leftValue = - i + 50;
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        seekBarRight.setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar, i: Int, b: Boolean) {
                rightValue = -i + 50;
            }
            override fun onStartTrackingTouch(seekBar: SeekBar) {}
            override fun onStopTrackingTouch(seekBar: SeekBar) {}
        })

        Thread(Runnable {

            while(true) {
                Thread.sleep(250)

                if (outputStream != null)
                    outputStream!!.write(byteArrayOfInts(leftValue, rightValue));

                this@MainActivity.runOnUiThread(java.lang.Runnable {
                    textViewStatus.text = "Tracks   " + leftValue + "  <--->  " + rightValue
                })
            }

        }).start()


    }

    fun byteArrayOfInts(vararg ints: Int) = ByteArray(ints.size) { pos -> ints[pos].toByte() }

    fun connect(view: View) {

        if (mBluetoothAdapter == null) {
            Toast.makeText(this@MainActivity, "Bluetooth not present", Toast.LENGTH_LONG).show()
            return
        }

        if (!mBluetoothAdapter.isEnabled()) {
            Toast.makeText(this@MainActivity, "Bluetooth disabled", Toast.LENGTH_LONG).show()
            return
        }


        try {

            val mmDevice = mBluetoothAdapter.getRemoteDevice(btAddress)
            val mmSocket = mmDevice.createRfcommSocketToServiceRecord(uuid)
            mmSocket.connect()
            outputStream = mmSocket.outputStream;

            Toast.makeText(this@MainActivity, "Connected", Toast.LENGTH_SHORT).show()

        } catch (e: Exception) {
            Toast.makeText(this@MainActivity, "Connection failed", Toast.LENGTH_SHORT).show()
        }

    }

}
