export const state = () => {
    return {
      families: [],
      isBusy: true,
      error: null
    }
  }
  
  export const getters = {
      FAMILIES: state => {
        return state.families
      },
      
      IS_BUSY: state => {
        return state.isBusy
      },
    
    ERROR: state => {
        return state.error
      }
  }
  export const mutations = {
    SET_FAMILIES: (state, families) => {
      state.families = families
      state.isBusy = false
    },
    
    SET_IS_BUSY: (state, isBusy) => {
      state.isBusy = isBusy
    },
    SET_ERROR: (state, error) => {
      state.error = error
    },
   
  
  }
  
  export const actions =  {
    async FETCH_FAMILIES(context) {
      context.commit('SET_IS_BUSY', true)
      try {
        const {data} = await this.$axios.get("/families")
        context.commit('SET_FAMILIES', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    async ADD_FAMILY(context,{payload }) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.post('/families',payload)
        const {data} = await this.$axios.get("/families")
        context.commit('SET_FAMILIES', data)
     
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    /*
    async UPDATE_CHILD(context,payload ) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.post(/children/${payload._id},payload)
         const {data} = await this.$axios.get("/childrens")
        context.commit('SET_CHILDREN', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    },
    async DELETE_CHILD(context,payload) {
      context.commit('SET_IS_BUSY', true)
      try {
        await this.$axios.delete(/children/deactivate-child/${payload._id})
         const {data} = await this.$axios.get("/childrens")
         context.commit('SET_CHILDREN', data)
      } catch(err) {
        context.commit('SET_IS_BUSY', false)
        context.commit('SET_ERROR', err)
      }
    }
  */
  }