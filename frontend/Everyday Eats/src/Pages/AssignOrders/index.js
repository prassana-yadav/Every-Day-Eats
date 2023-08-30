import axios from 'axios'
import OrderDetails from '../../Components/OrderDetails'
import { useEffect, useState } from 'react'
import NavAdmin from '../../Components/navAdmin'
import config from '../../config'
import { useNavigate } from 'react-router-dom'
const AssignOrders = () => {
  const [Orders, setdayWiseorder] = useState([])
const navigate=useNavigate();
  const Assignorders = () => {
    
    const url = config.serverURL+`/daywiseOrder/ListOfOrders`

    axios
      .post(url,{}, {
        headers: { Authorization: `Bearer ${localStorage['jwt']}` },
      })
      .then((response) => {
        const result = response.data
        debugger;
        if (result['status'] == 'success') {
          // navigate('/home')

          setdayWiseorder(result['data'])
        } else {
          alert(result.error)
        }
      })
  }

  const Save = () => {
    navigate('/Admin')
  }
  

  useEffect(() => {
    Assignorders()
  }, [])

  return (
    <div>
      <NavAdmin />
      <br />
      <h1 style={{ textAlign: 'center', color: 'white' }}>Orders List</h1>
      <br />
      <br />
      <div class='row'>
        <div class='col'></div>
        <div class='col-10'>
          <table class='table  table-dark table-striped'>
            <thead
              class='table-primary'
              style={{ background: 'grey', color: 'white' }}>
              <th>User Name</th>
              <th>User Address</th>
              <th>Area</th>
              <th>City</th>
              <th>Pincode</th>
              <th>OrderId</th>
              <th>Assign Delivery Boy</th>
            </thead>
            <tbody>
              {Orders.map((order) => {
                return <OrderDetails order={order} />
              })}
            </tbody>
          </table>

          <table class='table  table-dark table-striped'>
            <tbody>
            <tr><td >
              <center>
              <div className='col d-grid gap-2 d-md-flex justify-content-md-end'>

              <button
                className='btn btn-success'
                type='button'
                onClick={Save}

                >
                Save
              </button>
              </div>
              </center>
            
                </td>
                
                <td  >
             
              <button
                className='btn btn-primary'
                type='button'
                onClick={Save}
                >
                Back
              </button>
            
       
            </td>
                </tr>

            </tbody>
          </table>
        </div>
       
            
            
        <div class='col'></div>
      </div>
    </div>
  )
}
export default AssignOrders
