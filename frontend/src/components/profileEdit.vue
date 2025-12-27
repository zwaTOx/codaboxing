<template>
  <transition name="fade-modal" appear>
    <div class="modal-wrapper" @click.self="$emit('close')">
      <div class="modal__container modal-content">
        <h1 class="modal__container--title">Редактирование имени пользователя</h1>

        <div class="input__group">
          <label class="input__group--label">Введите имя пользователя</label>
          <input
            v-model="form.nickname"
            type="text"
            class="input__group--input"
          />
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
                nickname: useUserStore().user.nickname,
            },
            store: useUserStore()
        }
    },
    methods: {
        async handleSubmit() {
            try {
                const response = await useUserStore().changeUsername(this.form)

                if (response.success) {
                    this.$emit('update')
                } else {
                    console.log('response', response.error)
                }
            } catch (error) {
                console.log('catch', error)
            }
        }
    },
    mounted() {
        console.log('user data', this.form.new_username)

    }
}
</script>