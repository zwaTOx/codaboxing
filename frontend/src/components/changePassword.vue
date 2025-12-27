<template>
  <transition name="fade-modal" appear>
    <div class="modal-wrapper" @click.self="$emit('close')">
      <div class="modal__container modal-content">
        <h1 class="modal__container--title">Смена пароля</h1>

        <div class="input__group">
          <label class="input__group--label">Введите старый пароль</label>
          <input
            v-model="form.oldPassword"
            type="text"
            class="input__group--input"
          />
        </div>

        <div class="input__group">
          <label class="input__group--label">Введите новый пароль</label>
          <input
            v-model="form.newPassword"
            class="input__group--input"
          >
        </div>

        <div class="modal-actions" style="font-size: 20px; cursor: pointer; padding: 5px 10px; border-radius: 24px; color:#fff; background: var(--blue)" @click="handleSubmit">
            Сменить
        </div>
      </div>
    </div>
  </transition>
</template>


<script>
import { useUserStore } from '@/stores/user';

export default {
    data() {
        return {
            form: {
                oldPassword: '',
                newPassword: ''
            },
            store: useUserStore()
        }
    },
    methods: {
        async handleSubmit() {
            try {
                const response = await this.store.changePassword(this.form)

                if (response.success) {
                    this.$emit('close')
                } else {
                    console.log('response error', response.error)
                }
            } catch (error) {
                console.log('catch', error)
            }
        }
    }
}
</script>