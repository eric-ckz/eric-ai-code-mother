<template>
  <div id="userEditPage">
    <a-card title="编辑个人信息">
      <a-form :model="formState" :rules="rules" name="userEdit" @finish="handleSubmit">
        <a-form-item label="用户名" name="userName">
          <a-input v-model:value="formState.userName" placeholder="请输入用户名" />
        </a-form-item>
        <a-form-item label="头像" name="userAvatar">
          <a-input v-model:value="formState.userAvatar" placeholder="请输入头像URL" />
        </a-form-item>
        <a-form-item label="个人简介" name="userProfile">
          <a-textarea
            v-model:value="formState.userProfile"
            placeholder="请输入个人简介"
            :rows="4"
          />
        </a-form-item>
        <a-form-item>
          <a-button type="primary" html-type="submit" :loading="loading">保存</a-button>
          <a-button style="margin-left: 8px" @click="handleCancel">取消</a-button>
        </a-form-item>
      </a-form>
    </a-card>
  </div>
</template>

<script lang="ts" setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { message } from 'ant-design-vue'
import { getLoginUser, updateUser } from '@/api/userController.ts'
import { useLoginUserStore } from '@/stores/loginUser.ts'

const router = useRouter()
const loginUserStore = useLoginUserStore()
const loading = ref(false)

const formState = reactive<API.UserUpdateRequest>({
  id: undefined,
  userName: '',
  userAvatar: '',
  userProfile: '',
})

const rules = {
  userName: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
  userAvatar: [{ required: false, message: '请输入头像URL', trigger: 'blur' }],
  userProfile: [{ required: false, message: '请输入个人简介', trigger: 'blur' }],
}

// 加载当前用户信息
const loadCurrentUser = async () => {
  try {
    const res = await getLoginUser()
    if (res.data.code === 0 && res.data.data) {
      const user = res.data.data
      formState.id = user.id
      formState.userName = user.userName || ''
      formState.userAvatar = user.userAvatar || ''
      formState.userProfile = user.userProfile || ''
    } else {
      message.error('获取用户信息失败')
    }
  } catch (error) {
    message.error('获取用户信息失败')
  }
}

const handleSubmit = async (values: API.UserUpdateRequest) => {
  loading.value = true
  try {
    const res = await updateUser({
      ...values,
      id: formState.id,
    })
    if (res.data.code === 0) {
      message.success('更新成功')
      // 更新全局用户信息
      await loginUserStore.fetchLoginUser()
      router.push('/')
    } else {
      message.error('更新失败：' + res.data.message)
    }
  } catch (error) {
    message.error('更新失败')
  } finally {
    loading.value = false
  }
}

const handleCancel = () => {
  router.back()
}

onMounted(() => {
  loadCurrentUser()
})
</script>

<style scoped>
#userEditPage {
  max-width: 600px;
  margin: 0 auto;
  padding: 24px;
}
</style>