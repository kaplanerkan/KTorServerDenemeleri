package com.lotus.ktorserver.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.view.animation.Animation
import android.view.animation.AnimationSet
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import android.view.animation.TranslateAnimation
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.lotus.ktorserver.databinding.ItemUrunBinding
import com.lotus.ktorserver.models.Urun

class UrunAdapter : ListAdapter<Urun, UrunAdapter.UrunViewHolder>(DiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UrunViewHolder {
        val binding = ItemUrunBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return UrunViewHolder(binding)
    }

    override fun onBindViewHolder(holder: UrunViewHolder, position: Int) {
        val current = getItem(position)
        val previous = if (position < currentList.size) currentList[position] else null

        // Yeni eklenen veya değişen item mı?
        val isNewOrUpdated = previous == null || previous != current
        holder.bind(current, isNewOrUpdated)
    }

    class UrunViewHolder(private val binding: ItemUrunBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(urun: Urun, isNewOrUpdated: Boolean = false) {
            binding.apply {
                tvId.text = "ID: ${urun.id}"
                tvIsim.text = urun.urunIsmi
                tvFiyat.text = String.format("%,.2f ₺", urun.fiyati)
                tvActive.text = if (urun.active) "Aktif" else "Pasif"
                tvActive.setTextColor(
                    if (urun.active) root.context.getColor(android.R.color.holo_green_dark)
                    else root.context.getColor(android.R.color.holo_red_dark)
                )

                // YENİ VEYA GÜNCELLENEN ITEM İÇİN ANIMASYON
                if (isNewOrUpdated) {
                    root.startAnimation(createHighlightAnimation())
                }
            }
        }





        private fun createHighlightAnimation(): Animation {
            val anim = AnimationSet(true)

            // 1. Hafif büyüyüp küçülme (pulse)
            val scaleUp = ScaleAnimation(
                1f, 1.08f, 1f, 1.08f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleUp.duration = 200

            val scaleDown = ScaleAnimation(
                1.08f, 1f, 1.08f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f
            )
            scaleDown.duration = 150
            scaleDown.startOffset = 200

            // 2. Arka plan rengi parlasın (beyaz → sarımsı → geri)
            val alpha1 = AlphaAnimation(1f, 0.7f)
            alpha1.duration = 150

            val alpha2 = AlphaAnimation(0.7f, 1f)
            alpha2.duration = 300
            alpha2.startOffset = 350

            // 3. Hafif yukarı çıksın
            val translate = TranslateAnimation(0f, 0f, 50f, 0f)
            translate.duration = 400
            translate.interpolator = OvershootInterpolator()

            anim.addAnimation(scaleUp)
            anim.addAnimation(scaleDown)
            anim.addAnimation(alpha1)
            anim.addAnimation(alpha2)
            anim.addAnimation(translate)
            anim.duration = 650

            return anim
        }


    }

    class DiffCallback : DiffUtil.ItemCallback<Urun>() {
        override fun areItemsTheSame(oldItem: Urun, newItem: Urun): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Urun, newItem: Urun): Boolean {
            return oldItem == newItem  // ← BU SATIR ÇOK KRİTİK! data class olduğu için çalışıyor
        }
    }

}