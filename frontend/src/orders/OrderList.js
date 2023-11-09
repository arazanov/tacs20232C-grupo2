import React, {useEffect, useState} from 'react';
import {Button, ButtonGroup, Container, Table} from 'reactstrap';
import AppNavbar from '../navbar/AppNavbar';
import {useNavigate} from "react-router-dom";

function List({ orders, handleChange, remove }) {
    return orders.map(order => {
        return <tr key={order.id}>
            <td style={{whiteSpace: 'nowrap'}}>{order.description}</td>
            <td>{order.closed ? "Cerrado" : "Abierto"}</td>
            <td>
                <ButtonGroup>
                    <Button size="sm" color="primary" href={"/orders/" + order.id}
                            disabled={order.closed}>
                        Editar
                    </Button>
                    <Button size="sm" color="warning" onClick={() => handleChange({
                        ...order, closed: !order.closed
                    })} disabled={!order.owned}>
                        {order.closed ? "Abrir" : "Cerrar"}
                    </Button>
                    <Button size="sm" color="danger" onClick={() => remove(order.id)}>
                        Eliminar
                    </Button>
                </ButtonGroup>
            </td>
        </tr>
    });
}

export default function OrderList() {
    const [orders, setOrders] = useState([]);
    let token = localStorage.getItem('token');
    const navigate = useNavigate();

    useEffect(() => {
        fetch('/orders', {
            method: 'GET',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => {
                if (response.status === 401)
                    throw new Error(response.statusText);
                return response.json();
            })
            .then(data => {
                setOrders(data);
            })
            .catch(e => {
                console.log(e);
                alert("Autenticación incorrecta.");
            });
    }, [token]);

    function remove(id) {
        fetch("/orders/" + id, {
            method: 'DELETE',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        }).then(() => {
            setOrders(orders.filter(o => o.id !== id));
        });
    }

    function handleChange(order) {
        fetch("/orders/" + order.id, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                closed: order.closed
            })
        }).then(response => {
            if(response.ok)
                setOrders(orders.map(o => o.id === order.id ? order : o));
            else console.log(response.body);
        });
    }

    function createOrder() {
        fetch("/orders", {
            method: 'POST',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            }
        })
            .then(response => response.json())
            .then(data => {
                navigate("/orders/" + data.id);
            });
    }

    return (
        <div>
            <AppNavbar/>
            <Container fluid>
                <h3 style={{ paddingTop: 50 }}>Pedidos</h3>
                {
                    orders.length ?
                        <Table className="mt-4">
                            <thead>
                            <tr>
                                <th width="30%">Descripción</th>
                                <th width="30%">Estado</th>
                                <th width="40%">Acciones</th>
                            </tr>
                            </thead>
                            <tbody>
                            <List
                                orders={orders}
                                handleChange={handleChange}
                                remove={remove}
                            />
                            </tbody>
                        </Table> :
                        <div> No hay pedidos </div>
                }
                <div style={{ paddingTop: 50 }}>
                    <Button color="success" onClick={() => createOrder()}>
                        Crear pedido
                    </Button>
                </div>
            </Container>
        </div>
    );
}