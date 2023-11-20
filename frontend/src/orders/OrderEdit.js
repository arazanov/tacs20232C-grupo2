import {useParams} from "react-router";
import React, {useEffect, useState} from "react";
import {Button, Container, Form, FormGroup, Input, Label} from "reactstrap";
import AppNavbar from "../navbar/AppNavbar";
import {ItemList} from "../items/ItemList";
import {SuccessMessage} from "./SuccessMessage";
import {UserList} from "../share-users/UserList";
import {useNavigate} from "react-router-dom";
import {useAuth} from "../AuthContext";

export default function OrderEdit() {
    const {id} = useParams();
    const [order, setOrder] = useState({
        id: '',
        version: null,
        description: '',
        closed: false
    });
    const [items, setItems] = useState([]);
    const [users, setUsers] = useState([]);
    const [success, setSuccess] = useState(false);
    const {token} = useAuth();
    const navigate = useNavigate();

    useEffect(() => {
        function fetchConst(path, setter) {
            fetch("/orders/" + id + path, {
                method: 'GET',
                headers: {
                    'Accept': 'application/json',
                    'Content-Type': 'application/json',
                    'Authorization': 'Bearer ' + token
                }
            })
                .then(response => {
                    if (response.status === 401) {
                        console.log(token);
                        alert("No autorizado");
                        navigate("/");
                    }
                    return response.json();
                })
                .then(data => {
                    setter(data);
                });
        }

        fetchConst("", setOrder);
        fetchConst("/items", setItems);
        fetchConst("/users", setUsers);

    }, [id, token, navigate]);

    function handleSubmit(e) {
        e.preventDefault();

        fetch("/orders/" + id, {
            method: 'PATCH',
            headers: {
                'Accept': 'application/json',
                'Content-Type': 'application/json',
                'Authorization': 'Bearer ' + token
            },
            body: JSON.stringify({
                version: order.version,
                description: order.description
            })
        })
            .then(response => {
                if (response.status === 409 || response.status === 401)
                    throw new Error(response.statusText);
            })
            .then(() => setSuccess(true))
            .catch(e => {
                console.log(e);
                if (e.message.includes("Conflict")) {
                    alert("Pedido desactualizado, recargar página.");
                    window.location.reload();
                }
                if (e.message.includes("Unauthorized")) {
                    alert("Pedido cerrado");
                    navigate("/orders");
                }
            });
    }

    function createItem() {
        fetch("/orders/" + id + "/items", {
            method: 'POST',
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
                navigate("/orders/" + id + "/items/" + data.id);
            })
            .catch(e => {
                console.log(e);
                alert("Pedido cerrado");
                navigate("/orders");
            });
    }

    return <div>
        <AppNavbar/>
        <Container>
            <h2 style={{paddingTop: 50, paddingBottom: 50}}>Editar pedido</h2>
            <Form onSubmit={handleSubmit} style={{paddingBottom: 20}}>
                <FormGroup>
                    <Label for="description">Descripción</Label>
                    <Input type="text" id="description" defaultValue={order.description}
                           autoComplete={"off"}
                           onChange={e => {
                               setSuccess(false);
                               setOrder({...order, description: e.target.value});
                           }}/>
                </FormGroup>
                <SuccessMessage success={success}/>
                <FormGroup>
                    <Button color="primary" type="submit">Guardar</Button>
                </FormGroup>
            </Form>
            {
                items.length ?
                    <ItemList items={items} setItems={setItems} orderId={id}/> :
                    <div> No hay ítems </div>
            }
            <div style={{paddingTop: 30, paddingBottom: 50}}>
                <Button color="primary" onClick={() => createItem()}>Agregar ítem</Button>
            </div>
            {
                users.length ?
                    <UserList users={users} setUsers={setUsers} orderId={id}/> :
                    <div> No se compartió con ningún usuario </div>
            }
            <div style={{paddingTop: 30, paddingBottom: 50}}>
                <Button color="primary" href={"/orders/" + id + "/users"}>Compartir</Button>
            </div>
            <div style={{paddingBottom: 50}}>
                <Button color="success" href={"/orders"}>Volver</Button>
            </div>
        </Container>
    </div>
}