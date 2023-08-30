import React from 'react'
import Footcomponent from '../Components/footer'
import { Link } from 'react-router-dom'
import Kiran from '../assets/Kiran.jpg'
import Prassana from '../assets/Prassana.jpg'
import Mohit from '../assets/Mohit.jpg'
import Shubham from '../assets/Shubham.jpg'
import Navbar from './navHome'
const Aboutus = () => {
  return (
    <div>
      <div>
        <Navbar></Navbar>
      </div>
      <div style={{ backgroundColor: 'gainsboro' }}>
        <h1 style={{ textAlign: 'center' }}> About us</h1>
        <div
          className='container'
          style={{ paddingTop: 50, paddingBottom: 50 }}>
            
          <p>Welcome to our Tiffin Delivery Management System!</p>
          Delicious everyday food using choicest raw materials, delivered to your doorstep
          Home-style meals, prepared in state-of the art automated kitchen. The kitchen maintains the highest levels of hygiene and sanitization while handling food.
          Serving North-Indian, South-Indian and desi-Chinese cuisines, choose from Vegetarian and Non-Vegetarian meal options.
          Our cooks have more than a decade of experience in catering industry.
          <p>Join us in this journey to make every meal a delightful experience!</p>
          Sign up now and save big          <br />
          <br />
          <br />
          <br />
          <h1 style={{ textAlign: 'center' }}>Team</h1>
          <div class='row text-center'>
            <div class='col-xl-3 col-sm-6 mb-5'>
              <div class='bg-white rounded shadow-sm py-5 px-4'>
                <img
                  src={Prassana}
                  alt=''
                  width='100'
                  class='img-fluid rounded-circle mb-3 img-thumbnail shadow-sm'
                />
                <h5 class='mb-0'>Prassana Yadav (CEO)</h5>
                <span class='small text-uppercase text-muted'>
            
                </span>
                <a href='https://www.linkedin.com/in/prassana-yadav-4280aa244' class="btn btn-primary rounded-circle custom-icon"><div class='bi bi-linkedin ' style={{padding:1}}></div></a>
                
              </div>
            </div>

            <div class='col-xl-3 col-sm-6 mb-5'>
              <div class='bg-white rounded shadow-sm py-5 px-4'>
                <img
                  src={Mohit}
                  alt=''
                  width='100'
                  class='img-fluid rounded-circle mb-3 img-thumbnail shadow-sm'
                />
                <h5 class='mb-0'>Mohit Lokhande (CTO)</h5>
                <span class='small text-uppercase text-muted'>
                <a href='https://www.linkedin.com/in/mohit-lokhande-095027212' class="btn btn-primary rounded-circle custom-icon"><div class='bi bi-linkedin' style={{padding:1}}></div></a>

                </span>
              </div>
            </div>

            <div class='col-xl-3 col-sm-6 mb-5'>
              <div class='bg-white rounded shadow-sm py-5 px-4'>
                <img
                  src={Shubham}
                  alt=''
                  width='100'
                  class='img-fluid rounded-circle mb-3 img-thumbnail shadow-sm'
                />
                <h5 class='mb-0'>Shubham Suryawanshi (CEO)</h5>
                <span class='small text-uppercase text-muted'>
                </span>
                <a href='https://www.linkedin.com/in/prabhat-asai-8835a1218'class="btn btn-primary rounded-circle custom-icon"><div class='bi bi-linkedin' style={{padding:1}}></div></a>
              </div>
            </div>

            <div class='col-xl-3 col-sm-6 mb-5'>
              <div class='bg-white rounded shadow-sm py-5 px-4'>
                <img
                  src={Kiran}
                  alt=''
                  width='100'
                  class='img-fluid rounded-circle mb-3 img-thumbnail shadow-sm'
                />
                <h5 class='mb-0'>Kiran Katekar (CTO)</h5>
                <span class='small text-uppercase text-muted'>
                </span>
                <a href=' https://www.linkedin.com/in/kiran-katekar0055' class="btn btn-primary rounded-circle custom-icon"><div class='bi bi-linkedin' style={{padding:1}}></div></a>

              </div>
            </div>
          </div>
        </div>
        <Footcomponent />
      </div>
    </div>
  )
}




export default Aboutus
