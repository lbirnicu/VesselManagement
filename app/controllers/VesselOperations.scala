package controllers

/**
  * Mixes in all the *Vessel traits to offer a single access point for all operations on vessels
  */
trait VesselOperations
  extends AddVessel
  with FetchVessel
  with UpdateVessel
  with DeleteVessel
