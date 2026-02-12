package com.projetopessoal.agendamento.services;

import com.projetopessoal.agendamento.infrastructure.entity.Agendamento;
import com.projetopessoal.agendamento.infrastructure.repository.AgendamentoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class AgendamentoService {

   private final AgendamentoRepository agendamentoRepository;

   public Agendamento salvarAgendamento(Agendamento agendamento){

      LocalDateTime horaAgendamento = agendamento.getDataHoraAgendamento();
      LocalDateTime horaFim = agendamento.getDataHoraAgendamento().plusMinutes(1);

     Agendamento agendados = agendamentoRepository.findByServicoAndDataHoraAgendamentoBetween(agendamento.getServico(), horaAgendamento, horaFim);


     if(Objects.nonNull(agendados)){
        throw new RuntimeException("O horario ja está preenchido");

     }
      return agendamentoRepository.save(agendamento);

   }

   public void deletarAgendamento(LocalDateTime dataHoraAgendamento, String cliente){
      agendamentoRepository.deleteByDataHoraAgendamentoAndCliente(dataHoraAgendamento, cliente);
   }

   public List <Agendamento> buscarAgendamentosDia(LocalDate data){
      LocalDateTime primeiraHoraDia = data.atStartOfDay();
      LocalDateTime horaFinalDia = data.atTime(23, 59, 59);

      return agendamentoRepository.findByDataHoraAgendamentoBetween(primeiraHoraDia, horaFinalDia);
   }
   public Agendamento alterarAgendamento (Agendamento agendamento, String cliente, LocalDateTime dataHoraAgendamento ){
    Agendamento agenda = agendamentoRepository.findByDataHoraAgendamentoAndCliente(dataHoraAgendamento, cliente);

    if(Objects.isNull(agenda)){
         throw new RuntimeException("O horario não está preenchido");

      }

    agendamento.setId(agenda.getId());
     return agendamentoRepository.save(agendamento);

   }
}




