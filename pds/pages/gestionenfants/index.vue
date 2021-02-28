
<template> 
<div>
  <enfant-form></enfant-form>
  </div>
</template>

<script>
  import EnfantForm from '../../components/form/EnfantForm'

  export default {
    layout : "default",
    middleware: ['authenticated'],
    components: {
      EnfantForm
    }
  }
</script>
