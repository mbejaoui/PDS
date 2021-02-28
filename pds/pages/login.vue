<template>
  <login-form></login-form>
</template>

<script>
  import LoginForm from '../components/form/LoginForm'

  export default {
    middleware: 'notAuthenticated',
    layout : "landing",
    components: {
      LoginForm
    }
  }
</script>
