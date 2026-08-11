import React from 'react';
import { Text } from 'react-native';
import { NavigationContainer } from '@react-navigation/native';
import { createBottomTabNavigator } from '@react-navigation/bottom-tabs';
import { createNativeStackNavigator } from '@react-navigation/native-stack';

import { colors } from '../theme/colors';
import DashboardScreen from '../screens/DashboardScreen';
import EstoqueScreen from '../screens/EstoqueScreen';
import PedidosScreen from '../screens/PedidosScreen';
import FornecedoresScreen from '../screens/FornecedoresScreen';
import RelatoriosScreen from '../screens/RelatoriosScreen';
import ClientesScreen from '../screens/ClientesScreen';
import SolicitacoesScreen from '../screens/SolicitacoesScreen';

const Tab = createBottomTabNavigator();
const DashboardStack = createNativeStackNavigator();
const FornecedoresStack = createNativeStackNavigator();

const ICONES = {
  Dashboard: '🌿',
  Estoque: '📦',
  Pedidos: '🛒',
  Fornecedores: '🏭',
  'Relatórios': '📊',
};

function TabIcon({ nome, focused }) {
  return <Text style={{ fontSize: 20, opacity: focused ? 1 : 0.5 }}>{ICONES[nome]}</Text>;
}

// A tela inicial permite navegar também para Clientes, que não está na
// bottom-tab (o app segue as 5 abas principais: Dashboard, Estoque,
// Pedidos, Fornecedores e Relatórios).
function DashboardStackScreen() {
  return (
    <DashboardStack.Navigator screenOptions={{ headerStyle: { backgroundColor: colors.primary }, headerTintColor: '#fff' }}>
      <DashboardStack.Screen name="DashboardInicio" component={DashboardScreen} options={{ title: 'AgroBom' }} />
      <DashboardStack.Screen name="Clientes" component={ClientesScreen} options={{ title: 'Clientes' }} />
    </DashboardStack.Navigator>
  );
}

// Fornecedores + Solicitações de compra ficam na mesma aba, pois uma
// solicitação sempre está associada a um fornecedor.
function FornecedoresStackScreen() {
  return (
    <FornecedoresStack.Navigator screenOptions={{ headerStyle: { backgroundColor: colors.primary }, headerTintColor: '#fff' }}>
      <FornecedoresStack.Screen name="ListaFornecedores" component={FornecedoresScreen} options={{ title: 'Fornecedores' }} />
      <FornecedoresStack.Screen name="Solicitações" component={SolicitacoesScreen} options={{ title: 'Solicitações de Compra' }} />
    </FornecedoresStack.Navigator>
  );
}

export default function BottomTabs() {
  return (
    <NavigationContainer>
      <Tab.Navigator
        screenOptions={({ route }) => ({
          headerShown: false,
          tabBarActiveTintColor: colors.primary,
          tabBarInactiveTintColor: '#9e9e9e',
          tabBarIcon: ({ focused }) => <TabIcon nome={route.name} focused={focused} />,
        })}
      >
        <Tab.Screen name="Dashboard" component={DashboardStackScreen} />
        <Tab.Screen name="Estoque" component={EstoqueScreen} />
        <Tab.Screen name="Pedidos" component={PedidosScreen} />
        <Tab.Screen name="Fornecedores" component={FornecedoresStackScreen} />
        <Tab.Screen name="Relatórios" component={RelatoriosScreen} />
      </Tab.Navigator>
    </NavigationContainer>
  );
}
